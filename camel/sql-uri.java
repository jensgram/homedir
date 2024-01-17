public static String sqlUri(String sql, String... params) {
  var paramSuffix = (params.length > 0 ? "?" : "") + String.join("&", params);

  return sql
    .lines()
    .map(String::strip)
    .filter(Predicate.not(String::isEmpty))
    .collect(Collectors.joining(" ", "sql:", paramSuffix));
}

/*

sqlUri("""
  update imported_text set
    quality_score = :#quality_score
  where id = :#id
  """,
  "batch=true", "transacted=true"
)

*/
