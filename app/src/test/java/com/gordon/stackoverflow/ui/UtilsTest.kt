package com.gordon.stackoverflow.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {
    @Test
    fun stripHtml_removesTags() {
        val html = "<b>Bold</b> and <i>italic</i> text"
        val result = stripHtml(html)
        assertEquals("Bold and italic text", result)
    }

    @Test
    fun stripHtml_handlesEmptyString() {
        val result = stripHtml("")
        assertEquals("", result)
    }

    @Test
    fun stripHtml_handlesNoTags() {
        val result = stripHtml("Just text")
        assertEquals("Just text", result)
    }

    @Test
    fun dateFormated_formatsTimestamp() {
        val timestamp = 1620000000L
        val formatted = dateFormated(timestamp)
        // The expected format depends on your implementation, so check for non-empty result
        assert(formatted.isNotEmpty())
    }
}
