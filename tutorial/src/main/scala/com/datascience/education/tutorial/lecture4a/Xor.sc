import cats.data.Xor
val x = Xor.right(10)
val y = Xor.left("nope")
def times10 = (n: Int) => n * 10
x map times10
y map times10
y map times10 map times10
y leftMap println
def moreError = (err: String) => err + " more"
y leftMap moreError
def dontLike13(n: Int): Xor[String, Int] = {
  if (n == 13) Xor.left("yuck")
  else Xor.right(n) map times10
}
x flatMap dontLike13
y flatMap dontLike13
Xor.right(13) flatMap dontLike13

//4/0
Xor.catchOnly[ArithmeticException]{4/0}
