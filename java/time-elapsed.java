public static <T> T time(Supplier<T> subject) {
    var startTime = System.nanoTime();
    var result = subject.get();
    System.err.printf("Elapsed: %,.2fms%n", (System.nanoTime() - startTime) / 1_000_000f);

    return result;
}

