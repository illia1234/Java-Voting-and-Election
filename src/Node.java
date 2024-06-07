/**
 * Trieda reprezentujúca uzol pre spojkový zoznam.
 *
 * @param <T> typ dát uložených v uzle
 */
class Node<T> {
    T data;         // údaje uložené v uzle
    Node<T> next;   // referencia na nasledujúci uzol

    /**
     * Konštruktor pre vytvorenie uzlu s danými údajmi.
     *
     * @param data údaje uložené v uzle
     */
    public Node(T data) {
        this.data = data;
        this.next = null;
    }
}
