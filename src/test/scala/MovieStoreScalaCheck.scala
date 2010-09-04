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
import org.scalacheck._

object MovieStoreScalaCheck
    extends Properties("MovieStore") {

  import Gen._
  import Arbitrary._

  implicit def arbMovie: Arbitrary[Movie] =
    Arbitrary( for{
        title <- arbitrary[String]
        filmrating <- oneOf(6,12,16,18)
      } yield Movie(title,filmrating)
    )

  implicit def arbSetMovie: Arbitrary[Set[Movie]] =
    Arbitrary( for {
        pairs <- arbitrary[Set[(String,Int)]]
      } yield pairs map {case (t,f) => Movie(t,f)}
    )

  import Prop._

  property("Add") = forAll { m: Movie =>
    val movieStore = new MovieStore
    movieStore addToStore m
    movieStore.availableMovies.size == 1
    movieStore.rentMovies.size == 0
  }

  property("AddAndRent") = forAll { m: Movie =>
    val movieStore = new MovieStore
    movieStore addToStore m
    val keys = movieStore.availableMovies.keys
    movieStore rentMovie keys.head
    movieStore.availableMovies.size == 0
    movieStore.rentMovies.size == 1
  }

  property("AddAndTryRent") = forAll { m: Movie =>
    val movieStore = new MovieStore
    movieStore addToStore m
    val keys = movieStore.availableMovies.keys
    movieStore rentMovie (keys.head + 1)
    movieStore.availableMovies.size == 1
    movieStore.rentMovies.size == 0
  }

  property("AddAddRentReturn") = forAll {
    (m: Movie,n: Movie) =>
    val movieStore = new MovieStore
    movieStore addToStore m
    movieStore addToStore n
    val keys = movieStore.availableMovies.keys
    val serial = keys.head
    val movieOption = movieStore rentMovie serial
    movieOption match {
      case None => throw new NoSuchElementException
      case Some(movie) => movieStore returnMovie serial
    }
    movieStore.availableMovies.size == 2
    movieStore.rentMovies.size == 0
  }

  property("AddSet") = forAll {
    (ms: Set[Movie]) =>
    val movieStore = new MovieStore
    movieStore addToStore ms
    val setOfMovies: Set[Movie] =
      (movieStore.availableMovies :\ Set[Movie]()){
        case ((_,v),s) => s + v
      }
    setOfMovies == ms
  }

  property("AddSetAge") = forAll {
    (ms: Set[Movie], age: Int) =>
    val movieStore = new MovieStore
    movieStore addToStore ms
    val setOfMoviesForAge: Set[Movie] =
      (movieStore.availableMoviesForAge(age) :\
         Set[Movie]()){
        case ((_,v),s) => s + v
      }
    setOfMoviesForAge ==
         ms.filter{case Movie(_,f) => f <= age}
  }
}
