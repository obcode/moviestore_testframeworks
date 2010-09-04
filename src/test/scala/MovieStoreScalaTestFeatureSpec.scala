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
import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen
import org.scalatest.matchers.MustMatchers

class MovieStoreScalaTestFeatureSpec extends FeatureSpec
  with GivenWhenThen with MustMatchers {

  feature("The user can add a movie to the moviestore") {

    scenario("added one movie to an empty moviestore") {
      given("an empty moviestore")
      val movieStore = new MovieStore
      and("a movie")
      val m = Movie("Am Limit",6)
      when("added the movie to the moviestore")
      movieStore addToStore m
      then("availableMovies contains one movie")
      movieStore.availableMovies.size must be === 1
      and("rentMovies contains no movie")
      movieStore.rentMovies.size must be === 0
    }

    scenario("added one movie to an empty moviestore"+
        " and rent it") {
      given("an empty moviestore")
      val movieStore = new MovieStore
      and("a movie")
      val m = Movie("Am Limit",6)
      when("added the movie to the moviestore")
      movieStore addToStore m
      and("rent it")
      val keys = movieStore.availableMovies.keys
      movieStore rentMovie keys.head
      then("availableMovies contains no movie")
      movieStore.availableMovies.size must be === 0
      and("rentMovies contains one movie")
      movieStore.rentMovies.size must be === 1
    }
    scenario("added no movie, but try to get one from"+
        " availableMovies") {
      given("an empty moviestore")
      val movieStore = new MovieStore
      when("trying to get a movie from availableMovies")
      then("NoSuchElementException should be thrown")
      evaluating {
        movieStore.availableMovies(12)
      } must produce [NoSuchElementException]
    }
  }
}
