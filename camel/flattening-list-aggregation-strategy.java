public static final class FlatteningListAggregationStrategy extends AbstractListAggregationStrategy<List<?>> {
  @Override
  public List<?> getValue(Exchange exchange) {
    return exchange.getIn().getBody(List.class);
  }

  @Override
  public void onCompletion(Exchange exchange) {
    super.onCompletion(exchange);

    if (exchange != null) {
      var list = getValue(exchange);
      List<?> flattened = list == null
        ? List.of()
        : ((List<Collection<?>>) list).stream().flatMap(Collection::stream).toList();

      exchange.getIn().setBody(flattened);
    }
  }
}
