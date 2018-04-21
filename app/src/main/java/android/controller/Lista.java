package android.controller;

//Copiado del Juego

import android.hardware.Sensor;

import java.util.List;

public class Lista {
	//Atributos
	private Nodo head;
	private int length;
	//Constructor

	//Validador de Lista Vacia
	public boolean empty(){
		return head == null;
	}
	
	//Getters y Setters
	public Nodo getHead() {
		return head;
	}

	public void setHead(Nodo head) {
		this.head = head;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	//Metodos
	public void add(Sensor value){//Agregar un miembro a la hilera al final
		Nodo New = new Nodo();
		New.setValue(value);
		if (empty()){
			head = New;
		}
		else{
			Nodo aux = head;
			while(aux.getNext() != null){
				aux=aux.getNext();
			}
			aux.setNext(New);
		}
		length++;
	}
	public boolean search(Sensor reference){
		Nodo aux = head;
		boolean flag=false;
		while (aux != null && flag != true){
			if (reference == aux.getValue()){
				flag = true;
			}
			else{
				aux = aux.getNext();
			}
		}
	return flag;
	}
	public void delete(Sensor reference){//Elimina un miembro por referencia
		if (search(reference)){
			if (head.getValue() == reference){
				head = head.getNext();
			}
			else{
				Nodo aux = head;
				while(aux.getNext().getValue() != reference){
					aux = aux.getNext();
				}
				Nodo next = aux.getNext().getNext();
				aux.setNext(next);
			}
			length--;
		}
	}

	public void erase(){//Elimina la lista
		head = null;
		length = 0;
	}
	public Sensor getValue(int pos) {//Obtener valor por referencia
		if(pos >= 0 && pos < length) {
			if (pos == 0){
				return head.getValue();
			}else {
				Nodo aux = head;
				for (int i = 0; i < pos; i++) {
					aux = aux.getNext();
				}
				return aux.getValue();
			}
		}else {
			return null;
		}
	}
	public void swap(Sensor reference,int pos) {
		if(pos >= 0 && pos < length) {
			if (pos == 0){
				head.setValue(reference);
			}else {
				Nodo aux = head;
				for (int i = 0; i < pos; i++) {
					aux = aux.getNext();
				}
				aux.setValue(reference);
			}
		}
	}
	
}
