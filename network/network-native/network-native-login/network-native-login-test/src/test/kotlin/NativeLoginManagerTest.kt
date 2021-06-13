class NativeLoginManagerTest {

//    @Test
//    @Ignore
//    fun networkSuccess() = runBlocking {
//        val userSession = userSession("85cab69095196f3.89453480", "173984950848a2d27c0cc1c76ccf3d6d3dc8255b")
//        val meManager = NativeGetMeManager.Builder(OkHttpClient(), NativeGetMeDeserializer()).build()
//        val manager = NativeLoginManager.Builder(OkHttpClient(), NativeLoginDeserializer(), meManager).build()
//        val request = manager.request(userSession, "", "")
//        val response = manager.login(request)
//        println(response)
//    }
//
//    @Test
//    @Ignore
//    fun networkFailureInvalidMail() = runBlocking {
//        val userSession = userSession("85cab69095196f3.89453480", "173984950848a2d27c0cc1c76ccf3d6d3dc8255b")
//        val meManager = NativeGetMeManager.Builder(OkHttpClient(), NativeGetMeDeserializer()).build()
//        val manager = NativeLoginManager.Builder(OkHttpClient(), NativeLoginDeserializer(), meManager).build()
//        val request = manager.request(userSession, "a@b.com", "123")
//        val response = manager.login(request)
//        println(response)
//    }
//
//    @Test
//    @Ignore
//    fun networkFailureMissingMail() = runBlocking {
//        val userSession = userSession("85cab69095196f3.89453480", "173984950848a2d27c0cc1c76ccf3d6d3dc8255b")
//        val meManager = NativeGetMeManager.Builder(OkHttpClient(), NativeGetMeDeserializer()).build()
//        val manager = NativeLoginManager.Builder(OkHttpClient(), NativeLoginDeserializer(), meManager).build()
//        val request = manager.request(userSession, "", "123")
//        val response = manager.login(request)
//        println(response)
//    }
//
//    @Test
//    @Ignore
//    fun networkFailureMissingPassword() = runBlocking {
//        val userSession = userSession("85cab69095196f3.89453480", "173984950848a2d27c0cc1c76ccf3d6d3dc8255b")
//        val meManager = NativeGetMeManager.Builder(OkHttpClient(), NativeGetMeDeserializer()).build()
//        val manager = NativeLoginManager.Builder(OkHttpClient(), NativeLoginDeserializer(), meManager).build()
//        val request = manager.request(userSession, "my@mail.com", "")
//        val response = manager.login(request)
//        println(response)
//    }
}