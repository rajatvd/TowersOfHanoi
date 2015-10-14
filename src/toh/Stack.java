package toh;

public class Stack {
	
	int start,stop,top=start-1,size=0;
	int[] elems;// elements of the stack
	
	public Stack(int sstart,int sstop,int[] elemss){// to set the values of variables
		start = sstart;
		stop = sstop;
		elems = elemss;
		top = start-1;
	}
	
	public void push(int a){
		if(top+1>=stop)System.out.println(top+"StackOverFlow"+stop);//check if push is possible
		else{elems[++top]=a;size++;}
	}
	
	public int pop(){
		if(top<start){
			System.out.println(top+"StackUnderFlow"+start);//check if pop is possible
			return -1;
		}else{
			int a = elems[top];
			elems[top--]=0;
			size--;
			return a;// performs pop
		}
	}
	
	public Stack subStack(int s){
		if(s>=size){
			System.out.println(s+"Too big split!"+size);
			return null;
		}else{
			Stack st = new Stack(start+s,stop,elems);
			st.size = size-s;
			st.top = st.start+st.size-1;
//			size = s;
//			stop = st.start;
//			top = start+size-1;
			return st;
		}// to obtain a substack
	}
	
	public String toString(){
		String a = "";
		for(int i = start;i<stop;i++)a+=elems[i]+" ";
		return a;// to print the elements of stack
	}
	
}
