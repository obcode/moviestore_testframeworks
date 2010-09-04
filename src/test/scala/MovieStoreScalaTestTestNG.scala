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
import org.scalatest.testng.TestNGSuite

class MovieStoreScalaTestTestNG extends TestNGSuite {

  import org.testng.Assert._
  import org.testng.annotations.Test

  val m: Movie = Movie("Am Limit",6)

  @Test def add() {
    val movieStore = new MovieStore
    movieStore addToStore m
    assertEquals(movieStore.availableMovies.size,1)
    assertEquals(movieStore.rentMovies.size,0)
  }

  @Test def addAndRent() {
    val movieStore = new MovieStore
    movieStore addToStore m
    val keys = movieStore.availableMovies.keys
    movieStore rentMovie keys.head
    assert(movieStore.availableMovies.size == 0)
    assert(movieStore.rentMovies.size == 1)
  }

  @Test def exceptionTest() {
    val movieStore = new MovieStore
    intercept[NoSuchElementException] {
      movieStore.availableMovies(12)
    }
  }
}
