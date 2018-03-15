public class SocketIOTest {

    public static Map<String, Socket> map = Maps.newHashMap();

    public static void main(String[] args) {
        String username = UUID.randomUUID().toString().substring(0, 3);
        try {
            Socket socket = IO.socket("http://localhost:3001?lessonId=1&username=" + username);
            socket.on("new message", params -> {
                for(int i = 0; i < params.length; i++){
                    System.out.println(params[i].toString());
                }
            });

            socket.on("login", params -> {
                JSONObject jsonObject = (JSONObject) params[0];
                System.out.println("新用户加入， 当前用户数：" + jsonObject.get("numUsers"));
            });

            socket.on("user joined", params -> {
                JSONObject jsonObject = (JSONObject) params[0];
                System.out.println(jsonObject.get("username") + " 已加入, 当前用户数：" + jsonObject.get("numUsers"));

            });

            socket.on("user left", params -> {
                JSONObject jsonObject = (JSONObject) params[0];
                System.out.println(jsonObject.get("username") + " 已离开, 当前用户数：" + jsonObject.get("numUsers"));

            });

            socket.on(Socket.EVENT_CONNECT, params -> {
                socket.emit("add user", username);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message", "你好我是" + username);
                socket.emit("new message", jsonObject);
                System.out.println(username + " 上线啦");
            });

            socket.on(Socket.EVENT_DISCONNECT, params -> {
                System.out.println(username + " 下线啦");
            });

            socket.connect();
            map.put(username, socket);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}

