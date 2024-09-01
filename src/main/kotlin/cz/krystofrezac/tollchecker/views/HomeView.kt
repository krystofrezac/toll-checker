package cz.krystofrezac.tollchecker.views

import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.style
import kotlinx.html.textArea

fun kotlinx.html.HTML.homeView() {
    body {
        h1 {
            +"Kontrola platnosti dálničních známek"
        }

        form(action = "/check") {
            textArea {
                name = "licensePlates"
                style = "height:400px; width:200px;"
                placeholder = "Zadejte seznam SPZ. Každou SPZ dejte na nový řadek. "
            }
            button {
                +"Ověřit"
            }
        }
    }
}
