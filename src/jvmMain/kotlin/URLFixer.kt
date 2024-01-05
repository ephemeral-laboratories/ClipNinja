package garden.ephemeral.clipboard.ninja

/**
 * Generic interface for something which can fix a URL.
 */
interface URLFixer {
    /**
     * Fixes the URL.
     *
     * Implementations will typically look at the URL, and create a pair by calling either
     * [unmodified] or [modified].
     *
     * @param url the URL.
     * @return a pair containing, the URL, potentially modified, and optionally a
     *         string describing what modification was made.
     */
    fun fix(url: URL): Pair<URL, String?>

    /**
     * Convenience method to create a pair with the unmodified URL and no explanation.
     *
     * @param url the unmodified URL.
     * @return the pair.
     */
    fun unmodified(url: URL) = Pair(url, null)

    /**
     * Convenience method to create a pair with the modified URL and an explanation.
     *
     * @param url the modified URL.
     * @param explanation the explanation.
     * @return the pair.
     */
    fun modified(url: URL, explanation: String) = Pair(url, explanation)

}