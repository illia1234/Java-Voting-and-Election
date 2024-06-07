import java.util.ArrayList;
import java.util.List;

/**
 * Trieda LinkedList predstavuje jednosmerný zoznam.
 *
 * @param <T> typ dát uložených v zozname
 */
class LinkedList<T> {
    private Node<T> head; // Referencia na prvý prvok v zozname

    /**
     * Vytvára nový prázdny jednosmerný zoznam.
     */
    public LinkedList() {
        this.head = null;
    }

    /**
     * Pridáva nový prvok do zoznamu.
     *
     * @param data dáta nového prvku
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data); // Vytvorenie nového uzla s danými údajmi
        if (head == null) {
            head = newNode; // Ak je zoznam prázdny, nový uzol sa stáva hlavou zoznamu
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next; // Nájdenie posledného uzla v zozname
            }
            current.next = newNode; // Pripojenie nového uzla za posledný uzol
        }
    }

    /**
     * Konvertuje jednosmerný zoznam na zoznam ArrayList.
     *
     * @return zoznam ArrayList obsahujúci prvky z jednosmerného zoznamu
     */
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            list.add(current.data); // Pridáva dáta aktuálneho uzla do zoznamu ArrayList
            current = current.next; // Presun na nasledujúci uzol
        }
        return list;
    }
}
