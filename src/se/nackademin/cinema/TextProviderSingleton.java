package se.nackademin.cinema;

public class TextProviderSingleton {

    private static TextProviderSingleton instance;

    private TextProviderSingleton() {}

    public static TextProviderSingleton getInstance() {
        if (instance == null) {
            instance = new TextProviderSingleton();
        }
        return instance;
    }

    public final String CINEMA_INFO = """
                Cinema Name: QuantumTime Cinemas
                Location: Storgatan 51, Stockholm
                Phone Number: +46 8 555 123 456
                Opening Time: 14:00
                Closing Time: 02:00
                                
                Cutting-Edge Technology:
                                
                State-of-the-art laser projectors for crystal-clear visuals
                3D and 4D immersive experiences in select theaters
                Virtual Reality (VR) lounge for pre-show entertainment
                Special Events:
                                
                Time-Travel Thursdays: Classic films and cult favorites from various eras
                Quantum Nights: Exclusive screenings of avant-garde and experimental films
                Contactless Experience:
                                
                Mobile ticketing and contactless payment options
                Interactive holographic concierge for assistance
                Themed Auditoriums:
                                
                Steampunk Studio: A theater with a Victorian-era time-travel theme
                Cyber Future Hall: Futuristic aesthetics and cutting-edge technology
                Culinary Delights:
                                
                Molecular Gastronomy Popcorn Lab: Ever-changing popcorn flavors created with scientific flair
                Time Warp Café: A themed café offering international cuisine inspired by different time periods
                Environmental Initiatives:
                                
                Eco-friendly architecture with solar panels and green roofs
                Zero-waste initiatives, including compostable popcorn containers
                """;

        public final String MAIN_MENU = """
                1. Listed movies
                2. Cinema info
                3. Subscribe
                4. Exit""";


    public final String INNER_MENU = """
                1. Observe available seats
                2. Movie info
                3. Book seat
                4. Change seat
                5. Cancel seat
                6. Back""";

}
