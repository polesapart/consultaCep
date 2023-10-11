package com.github.polesapart.cep

import java.lang.Exception

/**
 * Mother of all exceptions from this library. For catch-all lazy folks such as me, myself and I.
 */
open class BaseCepApiErrorException(message: String) : Exception(message)

/**
 * A terminal error was reported from the API layer, i.e. the request object is invalid or nonexistent.
 */
class CepApiErrorException(message: String) : BaseCepApiErrorException(message)

/**
 * A transport-layer error was recorded.
 */
class HttpErrorException(message: String) : BaseCepApiErrorException(message)
