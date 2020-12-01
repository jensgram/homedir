package sql;

import javax.validation.constraints.NotNull;

public final class SqlLike {
  public static final String ESCAPE_CHAR = "_";

  private SqlLike() {}

  public static @NotNull String contains(@NotNull String value) {
    return "%" + escape(value) + "%";
  }

  private static @NotNull String escape(@NotNull String value) {
    return value
      .replace("_", ESCAPE_CHAR + "_")
      .replace("%", ESCAPE_CHAR + "%");
  }
}
