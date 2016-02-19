
	package vision;

	import java.io.BufferedReader;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.io.PrintWriter;
	import java.net.Socket;

	public class SocketClient
	{

		Socket imgSocket;
		PrintWriter out;
		BufferedReader in;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		public SocketClient()
		{
			String hostname = "127.0.0.1";
			int port = 82;

			try
			{
				imgSocket = new Socket(hostname, port);
				out = new PrintWriter(imgSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(
						imgSocket.getInputStream()));
				stdIn = new BufferedReader(new InputStreamReader(System.in));
		
				if(imgSocket.isConnected()) System.out.println("Connected");
			
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public String updateVision(){
			String inputText = null;
			
			try {
				inputText = stdIn.readLine();
				if((inputText) == null){
					System.out.println("Camera input null");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		
			}
			return inputText; 

		}
		
		
		public boolean isConnected(){ return imgSocket.isConnected();}
			}

