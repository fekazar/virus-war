package edu.bots.viruswar.game;

import edu.bots.viruswar.model.Figure;
import edu.bots.viruswar.model.Session;

public class GameUtils {
    // TODO: inject from properties
    private final int width;
    private final int height;
    private final int maxOps; // Max number of operations per move

    public GameUtils(int width, int height, int maxOps) {
        this.width = width;
        this.height = height;
        this.maxOps = maxOps;
    }

    public char[][] getEmptyField() {
        var res = new char[height][width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j)
                res[i][j] = Figure.EMPTY;
        }

        return res;
    }

    // Returns true, if update was successful
    public boolean update(Coordinates coords, char figure, Session session) {
        var field = session.getMappedField();

        // First move for CROSS player
        if (session.getMove() == 0) {
            // TODO: first operation should be in the corner???
            field[coords.y()][coords.x()] = figure;
            session.setMappedField(field);
            return true;
        }

        // First move for CIRCLE player
        if (session.getMove() == maxOps) {
            field[coords.y()][coords.x()] = figure;
            session.setMappedField(field);
            return true;
        }

        // Cannot put on the same figure
        if (field[coords.y()][coords.x()] == figure)
            return false;

        // Can put if there is same around
        if (hasSameAround(field, figure, coords)) {
            setOrKill(field, figure, coords);
            session.setMappedField(field);
            return true;
        }

        // Can put if the same is reachable through the chain of killed others
        if (dfs(coords.y(), coords.x(), figure, field, new boolean[height][width])) {
            setOrKill(field, figure, coords);
            session.setMappedField(field);
            return true;
        }

        // No conditions to put figure found
        return false;
    }

    public boolean canTurn(Session session, char figure) {
        if (session.getMove() <= maxOps) // to let players do their first moves
            return true;

        var finder = new AvailableFinder(session.getMappedField(), figure);
        return finder.hasAvailable();
    }

    private boolean hasSameAround(char[][] field, char figure, Coordinates coords) {
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                if (dx == 0 && dy == 0)
                    continue;

                int nx = coords.x() + dx;
                int ny = coords.y() + dy;

                if (nx < 0 || nx >= width || ny < 0 || ny >= height)
                    continue;

                if (field[ny][nx] == figure)
                    return true;
            }
        }

        return false;
    }

    private void setOrKill(char[][] field, char figure, Coordinates coords) {
        if (field[coords.y()][coords.x()] == Figure.EMPTY) {
            field[coords.y()][coords.x()] = figure;
            return;
        }

        field[coords.y()][coords.x()] = Figure.kill(field[coords.y()][coords.x()]);
    }

    // Returns true, if reached target through the chain of killed others
    // TODO: optimize
    private boolean dfs(int y, int x, char target, char[][] field, boolean[][] used) {
        used[y][x] = true;

        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                if (dx == 0 && dy == 0)
                    continue;

                int ny = y + dy;
                int nx = x + dx;

                if (ny < 0 || ny >= height || nx < 0 || nx >= width)
                    continue;

                if (field[ny][nx] == target)
                    return true;

                if (!used[ny][nx]
                        && field[ny][nx] == Figure.kill(Figure.other(target))
                        && dfs(ny, nx, target, field, used))
                    return true;
            }
        }

        return false;
    }

    private class AvailableFinder {
        private final char[][] field;
        private final char figure;
        private final boolean[][] used;
        private boolean found = false;

        public AvailableFinder(char[][] field, char figure) {
            this.field = field;
            this.figure = figure;
            used = new boolean[field.length][field[0].length];
        }

        public boolean hasAvailable() {
            for (int i = 0; i < field.length; ++i) {
                for (int j = 0; j < field[0].length; ++j) {
                    if (!used[i][j] && field[i][j] == figure && dfs(i, j))
                        return true;
                }
            }

            return false;
        }

        private boolean dfs(int y, int x) {
            used[y][x] = true;

            for (int dx = -1; dx <= 1; ++dx) {
                for (int dy = -1; dy <= 1; ++dy) {
                    if (dx == 0 && dy == 0)
                        continue;

                    int nx = x + dx;
                    int ny = y + dy;

                    if (nx < 0 || nx >= width || ny < 0 || ny >= height)
                        continue;

                    if (used[ny][nx])
                        continue;

                    if (field[ny][nx] == Figure.kill(figure))
                        continue;

                    if (field[ny][nx] == Figure.EMPTY || field[ny][nx] == Figure.other(figure)) {
                        return found = true;
                    }

                    var res = dfs(ny, nx);
                    if (res)
                        return true;
                }
            }

            return false;
        }
    }
}
