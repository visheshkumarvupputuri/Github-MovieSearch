package com.testapp.coding.moviesearch

import com.testapp.coding.moviesearch.networkcalls.MovieRetrofitService
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieSearchTest {

    // Test to verify MovieSearchDTO parsing
    @Test
    fun testMovieSearch() {
        val movieSearchDTO =
            MovieRetrofitService.create().searchMovies(MovieRetrofitService.API_KEY, "football", 1).execute()
                .body()
        assertEquals(8, movieSearchDTO?.totalPages)
        assertEquals(155, movieSearchDTO?.totalResults)
        assertEquals(1, movieSearchDTO?.page)
        assertEquals(195147, movieSearchDTO?.results?.get(0)?.id)
        assertEquals("Football", movieSearchDTO?.results?.get(0)?.title)
        assertEquals("1897-05-20", movieSearchDTO?.results?.get(0)?.releaseDate)
    }
}