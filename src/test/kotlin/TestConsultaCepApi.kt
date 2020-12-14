import com.aleques.cep.CepApiErrorException
import com.aleques.cep.ConsultaCep
import com.aleques.cep.HttpErrorException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TestConsultaCepApi {
    @OptIn(ExperimentalSerializationApi::class)
    private lateinit var cep: ConsultaCep

    @BeforeTest
    fun initCep() {
        cep = ConsultaCep()
    }

    @Test
    fun testGetInvalidCep() {
        var failed = true
        try {
            println(cep.getDataByCep("89160000"))
        } catch(e: CepApiErrorException) {
            println("caught expected error: $e")
            failed = false
        }
        assertEquals(false, failed)
    }

    @Test
    fun testGetInvalidFormatCep() {
        var failed = true
        try {
            println(cep.getDataByCep("8916000"))
        } catch(e: HttpErrorException) {
            println("caught expected error: $e")
            failed = false
        }
        assertEquals(false, failed)
    }

    @Test
    fun testGetCep() {
            println(cep.getDataByCep("80010200"))
    }
    @Test
    fun testGetDataByAddr1() {
        val r = cep.getDataByAddress("PR", "CURITIBA", "Adão Sobocinski")
        println(r)
    }
    @Test
    fun testGetDataByAddr2() {
        val r = cep.getDataByAddress("PR", "CURITIBA", "Souza Naves")
        println(r)
    }
}
