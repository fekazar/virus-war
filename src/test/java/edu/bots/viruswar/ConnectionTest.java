package edu.bots.viruswar;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.SessionRepository;
import edu.bots.viruswar.service.UpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class ConnectionTest {
    @Autowired
    private UpdateHandler updateHandler;

    @Autowired
    private SessionRepository sessionRepository;

    @MockBean
    private TelegramBot bot;

    private final Consumer<ServiceAnswer> onAnswer = serviceAnswer -> {
        log.info("to: " + serviceAnswer.sendTo() + " " + serviceAnswer.message());
    };

    @Test
    void canConnectTest() {
        var createHost = makeUpdate("/start", 1l, true);
        var createSession = makeUpdate("/create", 1l, true);
        var st = new ArrayList<String>();

        updateHandler.handle(createHost, onAnswer);
        // Weird way to get the output
        updateHandler.handle(createSession, onAnswer.andThen(serviceAnswer -> st.add(serviceAnswer.message())));

        // Session id has length 10
        var sessionId = st.get(st.size() - 1).substring(st.get(st.size() - 1).length() - 10);
        log.info("Session id: " + sessionId);

        assertNotNull(sessionId);

        var createClient = makeUpdate("/start", 2l, true);
        var connectSession = makeUpdate("/connect", 2l, true);
        var authSession = makeUpdate(sessionId, 2l, false);

        updateHandler.handle(createClient, onAnswer);
        updateHandler.handle(connectSession, onAnswer);
        updateHandler.handle(authSession, onAnswer);

        var createdSession = sessionRepository.findById(sessionId).get();
        assertEquals(1, createdSession.getHostId());
        assertEquals(2, createdSession.getClientId());
    }

    private Update makeUpdate(String msg, Long from, boolean isCommand) {
        Chat chat = mock(Chat.class);
        User user = mock(User.class);
        Message message = mock(Message.class);
        Update update = mock(Update.class);

        when(user.id()).thenReturn(from);
        when(chat.id()).thenReturn(from);
        when(message.from()).thenReturn(user);
        when(message.text()).thenReturn(msg);
        when(update.message()).thenReturn(message);

        if (isCommand) {
            MessageEntity commandEntity = mock(MessageEntity.class);
            when(commandEntity.type()).thenReturn(MessageEntity.Type.bot_command);
            when(commandEntity.offset()).thenReturn(0);
            when(commandEntity.length()).thenReturn(msg.length());

            when(message.entities()).thenReturn(new MessageEntity[]{commandEntity});
        }

        return update;
    }
}
