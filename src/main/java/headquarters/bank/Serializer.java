package headquarters.bank;

import com.google.gson.*;
import headquarters.bank.exceptions.TransactionAttributeException;

import java.lang.reflect.Type;

/**
 * Class serves as a custom serializer and deserializer for Transaction classes
 */
public class Serializer implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {
    /**
     * Deserializer converts JSON value to the java Transaction object, which can
     * further be used in methods and classes
     *
     * @param json    objects to be deserialized
     * @param typeOfT
     * @param context
     * @return deserialized ready to use object
     * @throws JsonParseException occurs if there is a problem parsing JSON string
     */
    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        JsonObject instanceObject = jsonObject.get("INSTANCE").getAsJsonObject();

        switch (jsonObject.get("CLASSNAME").getAsString()) {
            case "Payment":
                try {
                    return new Payment(
                            instanceObject.get("date").getAsString(),
                            instanceObject.get("amount").getAsDouble(),
                            instanceObject.get("description").getAsString(),
                            instanceObject.get("incomingInterest").getAsDouble(),
                            instanceObject.get("outgoingInterest").getAsDouble()
                    );
                } catch (TransactionAttributeException e) {
                    e.printStackTrace();
                }
                break;
            case "IncomingTransfer":
                try {
                    return new IncomingTransfer(
                            instanceObject.get("date").getAsString(),
                            instanceObject.get("amount").getAsDouble(),
                            instanceObject.get("description").getAsString(),
                            instanceObject.get("sender").getAsString(),
                            instanceObject.get("recipient").getAsString()
                    );
                } catch (TransactionAttributeException e) {
                    e.printStackTrace();
                }
                break;
            case "OutgoingTransfer":
                try {
                    return new OutgoingTransfer(
                            instanceObject.get("date").getAsString(),
                            instanceObject.get("amount").getAsDouble(),
                            instanceObject.get("description").getAsString(),
                            instanceObject.get("sender").getAsString(),
                            instanceObject.get("recipient").getAsString()
                    );
                } catch (TransactionAttributeException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }


    /**
     * Serializer converts Transaction objects to JSON values and saves them
     * in JSON files in the directory transactions
     *
     * @param src       object to be serialized
     * @param typeOfSrc
     * @param context
     * @return Json element, which is converted from object
     */
    @Override
    public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jObject = new JsonObject();
        JsonObject instanceValue = new JsonObject();

        jObject.addProperty("CLASSNAME", src.getClass().getSimpleName());

        switch (src.getClass().getSimpleName()) {
            case "Payment" -> {
                instanceValue.addProperty("incomingInterest", ((Payment) src).getIncomingInterest());
                instanceValue.addProperty("outgoingInterest", ((Payment) src).getOutgoingInterest());
            }
            case "IncomingTransfer" -> {
                instanceValue.addProperty("sender", ((IncomingTransfer) src).getSender());
                instanceValue.addProperty("recipient", ((IncomingTransfer) src).getRecipient());
            }
            case "OutgoingTransfer" -> {
                instanceValue.addProperty("sender", ((OutgoingTransfer) src).getSender());
                instanceValue.addProperty("recipient", ((OutgoingTransfer) src).getRecipient());
            }
        }

        instanceValue.addProperty("date", src.getDate());
        instanceValue.addProperty("amount", src.getAmount());
        instanceValue.addProperty("description", src.getDescription());
        jObject.add("INSTANCE", instanceValue);
        return jObject;
    }
}
