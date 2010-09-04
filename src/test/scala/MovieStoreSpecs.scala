/**
 * Copyright (c) 2010 Oliver Braun
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the author nor the names of his contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHORS ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE AUTHORS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
import org.specs._

object MovieStoreSpecs extends
    Specification("MovieStore Specification") {
  val movie = Movie("Step Across the Border",0)
  "A MovieStore" should {
    "when added one movie" >> {
      val movieStore = new MovieStore
      movieStore addToStore movie
      "contain one movie in availableMovies" >> {
        movieStore.availableMovies must have size 1
      }
      "contain no movie in rentMovies" >> {
        movieStore.rentMovies must have size 0
      }
    }
    "when added a set of movies" >> {
      val movieStore = new MovieStore
      movieStore addToStore Set(
        movie,
        Movie("Am Limit",6),
        Movie("The Matrix",16),
        Movie("Bad Taste",18),
        Movie("Bad Lieutenant",16)
      )
      "return only movies which are allowed"+
        " for the given age" >> {
        import org.specs.matcher.Matcher
        case class haveOnlyMoviesForTheAgeOf(age: Int)
            extends Matcher[Map[Int,Movie]] {
          def apply(movies: => Map[Int,Movie]) = {
            ((movies filter {
                case (_,(Movie(_,f))) => f > age
              }).isEmpty,
             "only allowed movies",
             "not only allowed movies"
            )
          }
        }
        import org.specs.specification.Result
        implicit def toOnlyMoviesForTheAgeOfResult(
            result: Result[Map[Int,Movie]])
          = new OnlyMoviesForTheAgeOfResult(result)
        class OnlyMoviesForTheAgeOfResult(
            result: Result[Map[Int,Movie]]) {
          def onlyMoviesForTheAgeOf(age: Int) =
            result.matchWithMatcher(
                haveOnlyMoviesForTheAgeOf(age))
        }
        val age = 15
        movieStore.availableMoviesForAge(age) must
          have onlyMoviesForTheAgeOf age
      }
    }
  }
}
