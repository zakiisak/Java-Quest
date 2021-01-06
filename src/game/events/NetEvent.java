package game.events;

import javax.swing.JOptionPane;

import game.Game;
import game.entity.MessageBox;
import game.entity.MessageBox.ChoiceEvent;
import game.entity.Player;
import game.entity.TileEvent;
import game.net.Network;
import game.utils.Event;
import game.worlds.World;

public class NetEvent extends TileEvent {
	
	public int customHostPort = Network.NetworkPort;
	
	@Override
	public void onStepped(final Game game, World world, Player player) {
		super.onStepped(game, world, player);
		boolean connected = game.client.isConnected();
		if(connected && game.server.isStarted())
		{
			MessageBox.addMessage("You are", "currently", "hosting a", "server. Close?").setChoices(new ChoiceEvent() {
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						Game.instance.server.stop();
						Game.instance.client.stop();
					}
				}
			}, "Yes", "No");
		}
		else if(connected)
		{
			MessageBox.addMessage("You are", "currently ", "connected..");
			MessageBox.addMessage("Leave?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(MessageBox box, String choice) {
					if(choice.equals("Yes"))
					{
						Game.instance.client.stop();
					}
				}
			}, "Yes", "No");
		}
		else
		{
			//Nothing is started
			MessageBox.addMessage("Welcome to the", "meeting place!");
			MessageBox.addMessage("Here you can", "connect with", "other players!");
			chooseAction(game);
		}
			
	}
	
	private void chooseAction(final Game game)
	{
		MessageBox.addMessage("Choose an", "action").setChoices(new ChoiceEvent() {
			@Override
			public void onChoice(final MessageBox box, String choice) {
				if(choice.equals("Join"))
				{
					MessageBox.addMessage("Press space", "and enter", "the ip:").setDoneEvent(new Event() {
						
						@Override
						public void run() {
							String ipString = JOptionPane.showInputDialog(null, "Please enter ip:", "IP Confirmation", JOptionPane.QUESTION_MESSAGE);
							if(ipString.isEmpty()) return;
							String[] ipAndPort = ipString.split(":");
							String ip = ipAndPort[0];
							int port = Network.NetworkPort;
							if(ipAndPort.length > 1)
							{
								try
								{
									port = Integer.parseInt(ipAndPort[1]);
								}
								catch(Exception e)
								{
									run();
									return;
								}
							}
							game.client.connect(ip, port, port);
							if(game.client.isCommunicationUp())
							{
								MessageBox.addMessage("Connected!").setDoneEvent(new Event() {
									
									@Override
									public void run() {
										game.client.sendStats();
									}
								});
								game.client.sendStats();
							}
							else
							{
								MessageBox.addMessage("Unable to", "connect to", "server");
								chooseAction(game);
							}
						}
					});
				}
				else if(choice.equals("Host"))
				{
					onHostAction(game);
				}
			}
		}, "Join", "Host", "Cancel");
	}
	
	
	private void onHostAction(final Game game)
	{
		game.server.start(customHostPort, customHostPort);
		game.client.connect("127.0.0.1", customHostPort, customHostPort);
		if(game.server.isStarted())
		{
			MessageBox.addMessage("Server Started", "on port: " + customHostPort);
		}
		else
		{
			MessageBox.addMessage("Unable to", "start server", "on port: " + customHostPort);
			MessageBox.addMessage("Choose a", "custom port?").setChoices(new ChoiceEvent() {
				
				@Override
				public void onChoice(final MessageBox box, final String choice) {
					if(choice.equals("Yes"))
					{
						String port = JOptionPane.showInputDialog(null, "Please enter custom port:", "Custom Port Specification", JOptionPane.QUESTION_MESSAGE);
						try
						{
							int portNumber = Integer.parseInt(port);
							customHostPort = portNumber;
							onHostAction(game);
						}
						catch(Exception e)
						{
							onChoice(box, choice);
							e.printStackTrace();
						}
					}
				}
			}, "Yes", "No");
		}
	
	}
	
	public boolean onInteraction(Game game, World world, Player player) {
		onStepped(game, world, player);
		return true;
	}
	
}
