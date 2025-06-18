public class BST {
    private Node root;

    private class Node {
        Patient patient;
        Node left, right;

        Node(Patient patient) {
            this.patient = patient;
        }
    }

    public void insert(Patient patient) {
        root = insert(root, patient);
    }

    private Node insert(Node node, Patient patient) {
        if (node == null) return new Node(patient);
        
        int cmp = patient.getID().compareTo(node.patient.getID());
        if (cmp < 0) {
            node.left = insert(node.left, patient);
        } else if (cmp > 0) {
            node.right = insert(node.right, patient);
        }
        
        return node;
    }

    public Patient search(String id) {
        return search(root, id);
    }

    private Patient search(Node node, String id) {
        if (node == null) return null;
        
        int cmp = id.compareTo(node.patient.getID());
        if (cmp < 0) {
            return search(node.left, id);
        } else if (cmp > 0) {
            return search(node.right, id);
        } else {
            return node.patient;
        }
    }

    public void delete(String id) {
        root = delete(root, id);
    }

    private Node delete(Node node, String id) {
        if (node == null) return null;
        
        int cmp = id.compareTo(node.patient.getID());
        if (cmp < 0) {
            node.left = delete(node.left, id);
        } else if (cmp > 0) {
            node.right = delete(node.right, id);
        } else {
            if (node.right == null) return node.left;
            if (node.left == null) return node.right;
            
            Node t = node;
            node = min(t.right);
            node.right = deleteMin(t.right);
            node.left = t.left;
        }
        
        return node;
    }

    private Node min(Node node) {
        if (node.left == null) return node;
        return min(node.left);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) return node.right;
        node.left = deleteMin(node.left);
        return node;
    }

    public void inorder() {
        inorder(root);
    }

    private void inorder(Node node) {
        if (node == null) return;
        inorder(node.left);
        System.out.println("ID: " + node.patient.getID());
        System.out.println("Nama: " + node.patient.getName());
        System.out.println("Umur: " + node.patient.getAge());
        System.out.println("Alamat: " + node.patient.getAddress());
        System.out.println("No HP: " + node.patient.getPhone());
        System.out.println("----------------------");
        inorder(node.right);
    }
}