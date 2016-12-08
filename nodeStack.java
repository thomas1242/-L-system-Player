class node {
    
    double x;
    double y;
    double delta;
    node next;
    
    public node(double x, double y, double delta) {
        this.x = x;
        this.y = y;
        this.delta = delta;
        next = null;
    }
    
    public double getDelta() {
        return this.delta;
    }
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
}

class nodeStack {
    
    node head;
    int size;
    
    public nodeStack() {
        head = null;
        size = 0;
    }
    
    public void push(double x, double y, double delta) {
        if( head == null ) {
            head = new node(x, y, delta);
        }
        else {
            node temp = new node(x, y, delta);
            temp.next = head;
            head = temp;
        }
        size++;
    }
    
    public node pop() {
        if( head == null ) {
            return null;
        }
        else {
            node temp = head;
            head = head.next;
            size--;
            return temp;
        }
    }
}