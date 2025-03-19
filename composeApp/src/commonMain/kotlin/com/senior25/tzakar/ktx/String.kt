package com.senior25.tzakar.ktx

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

inline fun String.ifEmpty(default:()-> String?): String?{
    return if (isEmpty()) default()  else this
}

fun String?.removeDoubleQuotes(): String {
    if (isNullOrEmpty()) return ""
    var outputText = this
    if (outputText.startsWith("\""))  outputText = outputText.substring(1)
    if (outputText.endsWith("\"")) {
        outputText = outputText.substring(0, outputText.length - 1)
    }
    if (outputText.startsWith("\"") || outputText.endsWith("\"")) {
        outputText.removeDoubleQuotes()
    }
    return outputText
}

inline fun <reified T> String.decodeJson(default:T? = null): T? = this?.let{
    Json.decodeFromString<T?>(this.replace("<null>","{}"))
}?:default

inline fun <reified T> T.encodeToJson()  = Json.encodeToString(this)

fun String.fixAvatarDbJson() =
    this.replace(Regex("(\\w+)="), "\"$1\":") // Replace '=' with ':'
        .replace(Regex("\\[([^\\]]+)\\]")) { matchResult ->
            val items = matchResult.groupValues[1].split(",").map { it.trim() }
            "[" + items.joinToString(", ") { "\"$it\"" } + "]"
        }.replace(Regex(",\\s*(?=\\w)"), ", ")


fun String.fixStringObjectJson() =
    this.replace("=", ":")  // Convert `=` to `:` for JSON format
        .replace(Regex("(\\w+):")) { matchResult ->  // Ensure keys are wrapped in double quotes
            val key = matchResult.groupValues[1]
            "\"$key\":"
        }
        .replace(Regex("([\\w-]+):")) { matchResult ->  // Ensure object keys are quoted
            val key = matchResult.groupValues[1]
            if (key.contains("-") || key.contains(":")) "\"$key\":" else key + ":"
        }
        .replace(":", ": ")  // Add space after colons for readability


@Serializable(with = AnySerializer::class)
data class EventWrapper(val data: Map<String, @Serializable(with = AnySerializer::class) Any>)

object AnySerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Any", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Any) {
        when (value) {
            is String -> encoder.encodeString(value)
            is Int -> encoder.encodeInt(value)
            is Double -> encoder.encodeDouble(value)
            is Boolean -> encoder.encodeBoolean(value)
            is List<*> -> encoder.encodeSerializableValue(ListSerializer(this), listOf(value))
            is Map<*, *> -> encoder.encodeSerializableValue(MapSerializer(String.serializer(), this), value as Map<String, Any>)
            else -> throw SerializationException("Unsupported type: ${value::class}")
        }
    }

    override fun deserialize(decoder: Decoder): Any {
        throw SerializationException("Deserialization is not supported for Any directly.")
    }
}