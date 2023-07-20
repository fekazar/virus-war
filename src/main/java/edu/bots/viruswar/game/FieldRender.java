package edu.bots.viruswar.game;

public class FieldRender {
    public static String render(char[][] field) {
        var sb = new StringBuilder();

        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j <  field[i].length - 1; ++j)
                sb.append(field[i][j]).append(" ");
            sb.append("\n");
        }

        return sb.toString();
    }
}
