def maxDifference(list: List[Int]): Int = list match {
  case Nil => -1
  case head :: tail => tail.foldLeft((head,head,-1))(updateDiffs)._3
}

def updateDiffs(tup: (Int, Int, Int), x: Int): (Int, Int, Int) =
  tup match {
    case (mn, mx, diff) if x < mn => (x, mx, mx - x) // a new low
    case (mn, mx, diff) if x > mx => (mn, x, x - mn) // a new high
    case _ => tup // element x was not higher or lower than elements previously encountered.  Maximum difference remains the same.
  }

maxDifference(List(2,3,10,2,4,8,1))
maxDifference(List(1,10,-10,100))
maxDifference(List(5))
