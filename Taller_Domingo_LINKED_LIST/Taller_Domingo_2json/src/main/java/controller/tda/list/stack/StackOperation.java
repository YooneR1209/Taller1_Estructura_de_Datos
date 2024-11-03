package controller.tda.list.stack;

import controller.tda.list.ListEmptyException;
import controller.tda.list.OverFlowException;
import controller.tda.list.LinkedList;

public class StackOperation <E> extends LinkedList<E> {
        private Integer top;

    public StackOperation(Integer top){ //Constructor de la clase
        this.top = top; //Inicializa la variable top
    }

    public Boolean verify() {
        return getSize().intValue() <= top.intValue(); //Verifica si el tamaño de la lista es menor o igual al tope
    }

    public void push(E dato) throws Exception { //Agrega un elemento al tope de la pila
        if (verify()) { //Verifica si la pila no está llena
            add(dato, 0); //Agrega el elemento al tope de la pila
        } else {
            throw new OverFlowException("Pila llena"); //Lanza una excepción si la pila está llena
        }
    }

    public E pop() throws ListEmptyException { //Elimina el elemento del tope de la pila
        if (!isEmpty()) { //Verifica si la pila no está vacía
            throw new ListEmptyException("Pila vacía"); //Lanza una excepción si la pila está vacía
        } else {
            return deleteFirst(); //Elimina el elemento del tope de la pila
        }
    }

    public Integer getTop() { //Obtiene el tope de la pila
        return top; //Devuelve el tope de la pila
    }

    public void setTop(Integer top) { //Establece el tope de la pila
        this.top = top; //Asigna el tope de la pila
    }


    
}