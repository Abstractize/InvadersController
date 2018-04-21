package android.controller;

//Copiado del juego

import android.hardware.Sensor;

public class Nodo {
	private Sensor value;
	private Nodo next;
	
	public Nodo(){
		this.setValue(null);
		this.setNext(null);
	}

	public Sensor getValue() {
		return value;
	}

	public void setValue(Sensor value) {
		this.value = value;
	}

	public Nodo getNext() {
		return next;
	}

	public void setNext(Nodo next) {
		this.next = next;
	}
}
