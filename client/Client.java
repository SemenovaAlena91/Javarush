package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.Connection;
import com.javarush.task.task30.task3008.ConsoleHelper;
import com.javarush.task.task30.task3008.Message;
import com.javarush.task.task30.task3008.MessageType;

import java.io.IOException;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;

    protected String getServerAddress(){
        ConsoleHelper.writeMessage("Введите адрес");
        String address = ConsoleHelper.readString();
        return address;
    }

    protected int getServerPort(){
        ConsoleHelper.writeMessage("Введите порт сервера");
        int port = ConsoleHelper.readInt();
        return port;
    }

    protected String getUserName(){
        ConsoleHelper.writeMessage("Введите имя пользователя");
        String userName = ConsoleHelper.readString();
        return userName;
    }

    protected boolean shouldSendTextFromConsole(){
        return true;
    }

    protected void sendTextMessage(String text){
        try{
            Message message = new Message(MessageType.TEXT, text);
            connection.send(message);
        }catch (IOException e){
            clientConnected = false;
        }
    }

    protected SocketThread getSocketThread(){
        return new SocketThread();
    }

    public class SocketThread extends Thread{
        protected void processIncomingMessage(String message){
            ConsoleHelper.writeMessage(message);
        }

        protected  void informAboutAddingNewUser(String userName){
            ConsoleHelper.writeMessage("Участник с именем "+ userName + " присоединился к чату.");
        };

        protected void informAboutDeletingNewUser(String userName){
            ConsoleHelper.writeMessage("Участник с именем "+ userName + " покинул чат.");
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected){
            synchronized (Client.this){
                Client.this.clientConnected = clientConnected;
                Client.this.notify();
            }
        }
    }

    public void run(){
        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.start();

                try{
                    synchronized (this) {
                        wait();
                    }

                }catch (InterruptedException e){
                    ConsoleHelper.writeMessage("Не удалось установить соединение");
                    return;
                }

        if(clientConnected) ConsoleHelper.writeMessage("Соединение установлено.\n" + "Для выхода наберите команду 'exit'.");
                else ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");

        String str = "";

        while (clientConnected){
            str = ConsoleHelper.readString() ;

            if(str == "exit") break;

            if(shouldSendTextFromConsole() == true){
                sendTextMessage(str);
            }
        }
    }

    public static void main(String[] args){
        Client client = new Client();
        client.run();
    }
}
