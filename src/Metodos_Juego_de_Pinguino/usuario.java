package Metodos_Juego_de_Pinguino;

import java.util.Random;

public class usuario {
			
		String nombre;
		String color;
		int posicion;
		int dados;
		int peces; 
		int bolasnieve;
		int id;
		
		
		public usuario(String nombre, String color) {
	        this.nombre = nombre;
	        this.color = color;
	        this.posicion = 0;
	        this.dados = 0;
	        this.peces = 0;
	        this.bolasnieve = 0;
	        this.id = id;
	    }
		
		
		public int getId() {
			return id;
		}
		
		 public String getNombre() {
		        return nombre; 
		    }

		    public void setNombre(String nombre) {
		        this.nombre = nombre; 
		    }

		    public String getColor() {
		        return color;	
		    }
		    
		    public void setColor(String color) {
		        this.color = color; 
		    }
		    
		    public int getPosicion() {
		        return posicion;	
		    }
		    
		    public void setposicion(int posicion) {
		        this.posicion = posicion; 
		    }

		    public int getDados() {
		        return dados;	
		    }
		    
		    public void setDados(int dados) {
		        this.dados = dados; 
		    }

		    
		    public int getPeces() {
		        return peces;	
		    }
		    
		    public void setPeces(int peces) {
		        this.peces = peces; 
		    }

		    public int getBolasNieve() {
		        return bolasnieve;	
		    }
		    
		    public void setBolasNieve(int bolasnieve) {
		        this.bolasnieve = bolasnieve; 
		    }



		 

		 public int tirarDado(boolean especial) {
		        Random r = new Random();
		        int resultado;
		        if (especial) {
		            if (r.nextDouble() < 0.2) {
		                resultado = r.nextInt(6) + 5;
		            } else {
		                resultado = r.nextInt(3) + 1;
		            }
		        } else {
		            resultado = r.nextInt(6) + 1;
		        }
		        return resultado;
		    }
		 
		 
		 
		 public void retrocederCasillas(int cantidad) {
			    this.posicion = Math.max(0, this.posicion - cantidad);  
		    }
			public void avanzarCasillas(int cantidad) {
			    this.posicion += cantidad;  
		    }
		}


		
		
		
	

