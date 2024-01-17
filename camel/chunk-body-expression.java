/**
 * Partitions the body (any {@link Map} or {@link Collection}) into chunks.
 *
 * @param chunks The (maximum) number of chunks
 * @return A body expression partitioning into a {@link Collection}
 *
 * @implNote The partitioning is performed as a simple round-robin.
 */
private static Expression chunkBody(final int chunks) {
  Assert.checkMinimumParameter("chunks", 1, chunks);

  return bodyExpression(body -> {
    if (chunks == 1) {
      return List.of(body);
    }

    final var counter = new AtomicInteger(0);
    final Map<Integer, ?> buckets;

    if (body instanceof Map<?, ?> map) {
      buckets = map.entrySet().stream()
        .collect(Collectors.groupingBy(
          entry -> counter.getAndIncrement() % chunks,
          Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
        ));
    } else if (body instanceof Collection<?> coll) {
      buckets = coll.stream()
        .collect(Collectors.groupingBy(entry -> counter.getAndIncrement() % chunks));
    } else {
      throw new IllegalArgumentException("Body neither Map nor Collection: " + body.getClass());
    }

    return buckets.values();
  });
}
