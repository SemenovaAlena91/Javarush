package com.javarush.task.task30.task3008;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Server {
    private static Map<String, Connection> connectionMap = new java.util.concurrent.ConcurrentHashMap<String, Connection>();
/*Класс Handler должен реализовывать протокол общения с клиентом.
Выделим из протокола отдельные этапы и реализуем их с помощью отдельных методов:

Этап первый - это этап рукопожатия (знакомства сервера с клиентом).
Реализуем его с помощью приватного метода String serverHandshake(Connection connection) throws IOException, ClassNotFoundException .
Метод в качестве параметра принимает соединение connection, а возвращает имя нового клиента.

Реализация метода должна:
1) Сформировать и отправить команду запроса имени пользователя
2) Получить ответ клиента
3) Проверить, что получена команда с именем пользователя
4) Достать из ответа имя, проверить, что оно не пустое и пользователь с таким именем еще не подключен (используй connectionMap)
5) Добавить нового пользователя и соединение с ним в connectionMap
6) Отправить клиенту команду информирующую, что его имя принято
7) Если какая-то проверка не прошла, заново запросить имя клиента
8) Вернуть принятое имя в качестве возвращаемого значения*/

    private static class Handler extends Thread{
        private Socket socket;

        private Handler(Socket socket){
            this.socket = socket;
        }

        private  String serverHandshake(Connection connection) throws IOException, ClassNotFoundException{

            while (true){
                connection.send(new Message(MessageType.NAME_REQUEST));

                Message message = connection.receive();

                if(message.getType() != MessageType.USER_NAME){
                    ConsoleHelper.writeMessage("тип сообщения неверный");
                    continue;
                }
                String userName = message.getData();
                if (userName.isEmpty()){
                    ConsoleHelper.writeMessage("имя пользователя пустое");
                    continue;
                }
                if (connectionMap.containsKey(userName)){
                    ConsoleHelper.writeMessage("полученное имя пользователя уже есть в списке");
                    continue;
                }
                connectionMap.put(userName,connection);
                connection.send(new Message(MessageType.NAME_ACCEPTED));
                return userName;
            }

        };
}

    public static void sendBroadcastMessage(Message message){
        for (Connection connection : connectionMap.values()) {
            try {
                connection.send(message);
            } catch (IOException e) {
                ConsoleHelper.writeMessage("не удалось отправить сообщение");
            }
        }
    }

    public static void main(String[] args){
        int port = ConsoleHelper.readInt();
        try(ServerSocket serverSocket = new java.net.ServerSocket(port)){

            ConsoleHelper.writeMessage("сервер запущен");

            while (true){
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                handler.start();
            }
        }catch (IOException e){
            ConsoleHelper.writeMessage("произошла ошибка");

        }
    }
}
