//引入socket.io模块
var io = require('socket.io')();

//定义在线人数
var numUsers = 0;

//监听客户端connection事件
io.on('connection', function(socket){
	console.log(socket.request);
	var addedUser = false;
    socket.on('new message', function(data){
		console.log(socket.handshake.query.username + '发送消息啦' + JSON.stringify(data));
		socket.broadcast.emit('new message', {
     		 username: socket.handshake.query.username,
     		 message: data
   		});
	});
    
    socket.on('add user', function(username){
		if(addedUser) return;
		
		socket.username = username;
		++numUsers;
		addUser = true;
		socket.emit('login', {
			numUsers: numUsers
		});
		socket.broadcast.emit('user joined', {
			username: socket.username,
			numUsers: numUsers
		});
	});

	socket.on('disconnect', function(){
		if(addedUser){
			--numUsers;
			socket.broadcast.emit('user left', {
				username: socket.username,
				numUsers: numUsers
			});
		}
	});
});

io.listen(3001, function(){
	console.log('Server listening at port 3001');
});

