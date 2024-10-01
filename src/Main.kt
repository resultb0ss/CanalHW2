import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

suspend fun main() {
    val text = Storage().text
    val str = text.split("!",",",".",":",";").map { it.split(" ").toList() }
    val channel = Channel<String>()
    val time = measureTimeMillis {
        coroutineScope {
            val chanelOne = getList(str)
            val channelTwo = modifiedList(chanelOne)
            val channelThree = modifiedString(channelTwo)
            val stringArray: ArrayList<CharArray> = arrayListOf()
            val stringArrayTwo: ArrayList<Char> = arrayListOf()
//            channelTwo.consumeEach {
//                println("Получение данных $it")
//            }
            channelThree.consumeEach {
                if (it.size > 1){
                    stringArrayTwo.add(it[0])
                }
                stringArray.add(it)
                println("Получение данных ${it.contentToString()}")
            }

            println(stringArrayTwo)
        }
    }
    println("Время затраченное на выполнение: $time")


}

suspend fun CoroutineScope.getList(strList: List<List<String>>): ReceiveChannel<String> = produce {
    for (row in strList){
        for (j in row){
            delay(100L)
            send(j)
        }
    }
    channel.close()
}

suspend fun CoroutineScope.modifiedList(chanel: ReceiveChannel<String>) : ReceiveChannel<String> = produce {
    chanel.consumeEach {
        send(
            it.replaceFirstChar { it.uppercase() }
        )
    }
}

suspend fun CoroutineScope.modifiedString(chanel: ReceiveChannel<String>) : ReceiveChannel<CharArray> = produce {
    chanel.consumeEach {
        send(
            it.toCharArray()
        )
    }
}