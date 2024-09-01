package cz.krystofrezac.tollchecker.views

import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.span
import kotlinx.html.style

fun kotlinx.html.HTML.resultView(results: Map<String, Boolean>) {
    body {
        h1 { +"Výsledek kontroly" }

        div {
            results.map {
                style = "display:flex; flex-direction:column;"

                span {
                    val validText = if (it.value) "Validní" else "Nevalidní"
                    val color = if (it.value) "green" else "red"

                    style = "color:$color;"
                    +"${it.key} - $validText"
                }
            }
        }
    }
}
