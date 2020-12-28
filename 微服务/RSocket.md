request/response (stream of 1) 一对一

request/stream (finite stream of many) 一个请求多个相应

fire-and-forget(no response) 服务端没相应

event  subscription (infinite stream of many) 事件订阅，无限个连上一直返回信息

-----

ReferenceCounted.java 计数器

refCnt();引用了几次

retain()  一个引用过来+1

retain(int increment) 一堆引用加 N

touch()用来做记录



PayLoad.java（payload of a frame ) 



netty ->  ByteBuf  data();

nio -> getDate(); 默认情况下

metadata() 

FluxBuffer.java

scanUnsafe(Attr key[parent , bufferd ..]) { }