package net.lds.lib

import com.eygraber.uri.Uri
import com.eygraber.uri.encodeUri
import com.eygraber.uri.toKmpUrl
import io.ktor.http.encodeURLPath
import io.ktor.http.encodeURLQueryComponent
import java.net.URLEncoder

class MyClass {

    //..https%3A%2F%2F1.example.com%2F1+%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82+%D0%BC%D0%B8%D1%80+.mp4 /1 привет мир .mp4

    init{
        //val x = "https://1.example.com/1 привет мир .mp4"
        val x = "http://online.cinema.ntop.tv/hd/planet_earth/Episode 3: Fresh Water.mp4?1=1&2=2"

        val res = URLEncoder.encode(x, "UTF-8") //Это кодирует


        val url = io.ktor.http.Url(x).encodedPathAndQuery//.. Это нет



        //android.net.Uri.parse()
       val kmp1 =  com.eygraber.uri.Uri.parse(x)
        val kmp2 =  x.toKmpUrl()



        println("${res} $url || $kmp1 || $kmp2 ")
        println("${x.encodeUri()}")
        println("https://example.com?q=1 2".encodeUri())

        println("${normalizeMediaUrl(x)}") // https://1.example.com/1 привет мир .mp4 !!!!
    }

    companion object{

        @JvmStatic
        fun main(args: Array<String>) {
            println("Arguments passed: ${args.joinToString()}")
            MyClass()
        }
    }
}


fun normalizeMediaUrl(rawUrl: String): String {
    val url = io.ktor.http.Url(rawUrl)


// 1. PATH — кодируем сегменты
    val encodedPath = url.pathSegments
        .filter { it.isNotEmpty() }
        .joinToString("/") { it.encodeURLPath() }

    // 2. QUERY — кодируем ключи и значения
    val encodedQuery = url.parameters.entries()
        .flatMap { (key, values) ->
            values.map { value ->
                "${key.encodeURLQueryComponent()}=${value.encodeURLQueryComponent()}"
            }
        }
        .joinToString("&")

    return buildString {
        append(url.protocol.name)
        append("://")
        append(url.host)
        if (encodedPath.isNotEmpty()) {
            append("/")
            append(encodedPath)
        }
        if (encodedQuery.isNotEmpty()) {
            append("?")
            append(encodedQuery)
        }
    }
}

//normUrl http://online.cinema.ntop.tv/hd/planet_earth/Episode 3: Fresh Water.mp4 || http://online.cinema.ntop.tv/hd/planet_earth/Episode 3: Fresh Water.mp4

//fun normalizeMediaUrl(rawUrl: String): String {
//    val url = io.ktor.http.Url(rawUrl)
//    val res = buildString {
//        append(url.protocol.name)
//        append("://")
//        append(url.host)
//        if (url.port != url.protocol.defaultPort) {
//            append(":${url.port}")
//        }
//        append(url.encodedPath)
//        if (url.encodedQuery.isNotEmpty()) {
//            append("?")
//            append(url.encodedQuery)
//        }
//    }
//    //val res = URLEncoder.encode(rawUrl, "UTF-8")
//    val uri = Uri.parse(rawUrl)
//    val res = uri.toString()
//    println("normUrl $uri $rawUrl || $res")
//    return res
//}