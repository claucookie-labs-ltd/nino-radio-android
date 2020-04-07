package dev.claucookielabs.ninoradio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        thanks_text.text = buildSpannedString {
            bold { appendln("Director y locutor:") }
            appendln("Roberto Perez")
            appendln()
            bold { appendln("Reporteros:") }
            appendln("Álvaro Pérez")
            appendln("Guillermo Garcia-Junco Jr.")
            appendln("Victor Beltran")
            appendln("Fabio Ceriali")
            appendln("Pedro Lissen")
            appendln("Fco Javier Nuñez")
            appendln("Laura Cobo")
            appendln("Aida Ramos")
            appendln()
            bold { appendln("Marketing y publicidad:") }
            appendln("Ignacio Martin")
            appendln("David Martin")
            appendln()
            bold { appendln("RRSS:") }
            appendln("María Vega")
            appendln("Patricia Haurie")
            appendln()
            bold { appendln("Meteorología:") }
            appendln("Juan Beltrán")
            appendln()
            bold { appendln("Colaboradores especiales:") }
            appendln("Guillermo Garcia-Junco")
            appendln("María Vega Cabello ")
            appendln()
            bold { appendln("Apoyo emocional:") }
            appendln("Alejandra Simón")
            appendln()
            bold { appendln("Desarrollo de la app:") }
            appendln("Claudia Luque")
        }

        app_version.text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showLinkedin(view: View?) {
        openUrl("https://www.linkedin.com/in/claucookie/")
    }

    fun showTwitter(view: View?) {
        openUrl("https://twitter.com/clau_cookie")
    }

    private fun openUrl(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}
