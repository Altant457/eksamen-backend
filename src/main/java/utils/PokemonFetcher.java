package utils;

public class PokemonFetcher {

//    public static PokemonDTO getData(String query) throws IOException {
//        System.out.println(query);
//        try {
//            String pokeJSON = HttpUtils.fetchData(String.format("https://pokeapi.co/api/v2/pokemon/%s/", query));
//            JsonObject json = JsonParser.parseString(pokeJSON).getAsJsonObject();
//            String id = json.get("id").getAsString();
//            String name = json.get("species").getAsJsonObject()
//                    .get("name").getAsString();
//            String imageURL = json.get("sprites").getAsJsonObject()
//                    .get("other").getAsJsonObject()
//                    .get("official-artwork").getAsJsonObject()
//                    .get("front_default").getAsString();
//            return new PokemonDTO(id, name, imageURL);
//        } catch (FileNotFoundException e) {
//            throw new WebApplicationException("Pokémon not found", 404);
//        }
//    }
}
