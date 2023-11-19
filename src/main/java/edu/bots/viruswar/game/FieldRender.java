package edu.bots.viruswar.game;

public class FieldRender {
    public static String render(char[][] field) {
        var sb = new StringBuilder();

        sb.append("`\n");
        sb.append("    A B C D E F G H I J\n");

        for (int i = 0; i < field.length; ++i) {
            sb.append(String.format("%-4d", i + 1));

            for (int j = 0; j < field[i].length - 1; ++j)
                sb.append(field[i][j]).append(" ");

            sb.append(field[i][field[i].length - 1]);
            sb.append("\n");
        }

        sb.append("`");

        return sb.toString();
    }
}
