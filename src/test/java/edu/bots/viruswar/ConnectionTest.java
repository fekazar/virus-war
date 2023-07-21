package edu.bots.viruswar;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.repository.SessionRepository;
import edu.bots.viruswar.service.UpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.concurrent.*;
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

    @Autowired
    private PlayerRepository playerRepository;

    @MockBean
    private TelegramBot bot;

    private final Consumer<ServiceAnswer> logAnswer = serviceAnswer -> {
        log.info("to: " + serviceAnswer.sendTo() + " " + serviceAnswer.message());
    };

    private final Consumer<ServiceAnswer> devNull = serviceAnswer -> {};

    @Test
    @Order(1)
    void canConnectTest() throws Exception {
        var createHost = makeUpdate("/start", 1l, true);
        var createSession = makeUpdate("/create", 1l, true);
        var st = new ArrayBlockingQueue<String>(1);

        awaitUpdate(createHost, logAnswer);
        // Weird way to get the output
        awaitUpdate(createSession, logAnswer.andThen(serviceAnswer -> st.add(serviceAnswer.message())));

        // Session id has length 10
        var str = st.take();
        var sessionId = str.substring(str.length() - 10);
        log.info("Session id: " + sessionId);

        assertNotNull(sessionId);

        var createClient = makeUpdate("/start", 2l, true);
        var connectSession = makeUpdate("/connect", 2l, true);
        var authSession = makeUpdate(sessionId, 2l, false);

        awaitUpdate(createClient, logAnswer);
        awaitUpdate(connectSession, logAnswer);
        awaitUpdate(authSession, logAnswer);

        var createdSession = sessionRepository.findById(sessionId).get();
        assertEquals(1, createdSession.getHostId());
        assertEquals(2, createdSession.getClientId());
    }

    @Test
    @Order(2)
    void movesTest() throws Exception {
        var sendCoords1 = makeUpdate("a 1", 1l, false);
        var sendCoords2 = makeUpdate("b 1", 1l, false);
        var sendCoords3 = makeUpdate("a 2", 1l, false);

        awaitUpdate(sendCoords1, logAnswer);
        awaitUpdate(sendCoords2, logAnswer);
        awaitUpdate(sendCoords3, logAnswer);
    }

    @Test
    void disconnectTest() throws Exception {
        var createHost = makeUpdate("/start", 4l, true);
        var createSession = makeUpdate("/create", 4l, true);
        var st = new ArrayList<String>();

        awaitUpdate(createHost, devNull);
        awaitUpdate(createSession, devNull.andThen(serviceAnswer -> st.add(serviceAnswer.message())));

        // Session id has length 10
        var sessionId = st.get(st.size() - 1).substring(st.get(st.size() - 1).length() - 10);
        log.info("Session id: " + sessionId);

        assertNotNull(sessionId);

        var createClient = makeUpdate("/start", 5l, true);
        var connectSession = makeUpdate("/connect", 5l, true);
        var authSession = makeUpdate(sessionId, 5l, false);

        awaitUpdate(createClient, devNull);
        awaitUpdate(connectSession, devNull);
        awaitUpdate(authSession, devNull);

        var createdSession = sessionRepository.findById(sessionId).get();
        assertEquals(4, createdSession.getHostId());
        assertEquals(5, createdSession.getClientId());

        var disconnectClient = makeUpdate("/disconnect", 5l, true);
        awaitUpdate(disconnectClient, logAnswer);

        var sessionOpt = sessionRepository.findById(sessionId);
        assertTrue(sessionOpt.isEmpty());

        var host = playerRepository.findById(createdSession.getHostId()).get();
        var client = playerRepository.findById(createdSession.getClientId()).get();

        assertEquals(Player.State.DEFAULT, host.getState());
        assertEquals(Player.State.DEFAULT, client.getState());
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

    private void awaitUpdate(Update update, Consumer<ServiceAnswer> onAnswer) throws Exception {
        var latch = new CountDownLatch(1);
        updateHandler.handle(update, onAnswer.andThen(serviceAnswer -> latch.countDown()));
        latch.await();
        Thread.sleep(200); // cringe :(
    }
}
