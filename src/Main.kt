import kotlinx.coroutines.flow.*
import kotlin.random.Random
import kotlin.random.nextInt

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
suspend fun main() {
    println("==================================1==================================")
    val sum = numbers.map { it * it }.reduce { a, b -> (a + b) }
    println("Сумма = $sum")

    println("==================================2==================================")
    println("Введите первую букву имени")
    val symbol = readln().toUpperCase()
    println("Введите возраст")
    val age = readln().toInt()
    getPerson(symbol, age).collect{ println(it)}
    println("==================================3==================================")


    getUser(userName, getCart(), getPassword()){name, cart, password->
        User(name, cart, password)
    }.collect{ println(it)}
}


val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9).asFlow()

data class Person(val name: String, val age: Int)
data class User(val name: String, val cart: String, val password: Int){
    override fun toString(): String = "Пользователь: имя = $name, карта = $cart, пароль = $password"
}

val personList = listOf(
    Person("Алексей", 25),
    Person("Андрей", 34),
    Person("Василиса", 29),
    Person("Екатерина", 30),
    Person("Дмитрий", 18),
    Person("Софья", 12),
    Person("Максим", 38),
    Person("Николай", 24),
    Person("Константин", 25),
    Person("Мария", 30)
).asFlow()

fun getPerson(first: String, age: Int) = flow<Person>{
    personList.filter {
        it.name.first().toString() == first
                && it.age == age
    }.collect{
        println("${it.name}: возраст ${it.age}")
    }
}

val userName = listOf("Алексей", "Андрей", "Екатерина", "Максим", "Мария").asFlow()

suspend fun getCart(): Flow<String> {
    var length = 0
    userName.collect{length = it.length}
    val random = Random
    val symbols = "1234567890".toCharArray()
    val cartList = mutableListOf<String>()
    for (i in 0..< length){
        var numCart = ""
        for (j in 0..18){
            numCart += when (j) {
                4, 9, 14 -> " "
                0 -> random.nextInt(2..9).toString()
                else -> symbols[random.nextInt(symbols.size)]
            }
        }
        cartList.add(numCart)
    }
    return cartList.asFlow()
}

suspend fun getPassword(): Flow<Int>{
    var length = 0
    userName.collect{length = it.length}
    val passwordList = mutableListOf<Int>()
    val random = Random
    for (i in 0 ..<length){
        passwordList.add(random.nextInt(1000..9999))
    }
    return passwordList.asFlow()
}

suspend fun getUser (
    first: Flow<String>,
    second: Flow<String>,
    third: Flow<Int>,
    transform:(String, String, Int)-> User
): Flow<User> {
    val userList = mutableListOf<User>()
    first.zip(second){a,b->
        a to b
    }.zip(third){(a, b), c ->
        transform(a, b, c)
    }.collect{userList.add(it)}
    return userList.asFlow()
}


