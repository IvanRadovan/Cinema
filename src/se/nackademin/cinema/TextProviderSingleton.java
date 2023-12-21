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
            Welcome to CineClassics, your private escape into the world of timeless cinema.
            Nestled in the heart of Stockholm, our boutique cinema offers a unique blend of contemporary blockbusters and beloved classics,
            providing a cinematic experience that caters to both the avid moviegoer and those seeking a nostalgic trip down memory lane.
              
            Location: Storgatan 51, Stockholm
            Phone Number: +46 8 555 123 456
            E-mail: info@cineclassics.com
            Website: www.cineclassics.com
            Capacity: 64 Seats
                        
            About Us:
            At CineClassics, we take pride in being more than just a cinema – we are a celebration of film history.
            Our carefully curated selection includes the latest releases for the discerning movie buff,
            and our dedication to showcasing cinematic masterpieces from the past sets us apart.
                            
            Whether you're in the mood for the latest Hollywood blockbuster.
            Or yearning for the timeless charm of cinematic classics like "The Godfather" and "The Matrix",
            CineClassics Cinema is your destination for a diverse range of films that transcend time.
                            
            Come join us for an unparalleled cinematic experience where every frame tells a story and every visit creates memories.
            We look forward to welcoming you to CineClassics Cinema, where the magic of movies comes to life!
            """;

    public final String WEEKLY_OPENING_HOURS = """
            Opening Hours:
            Monday:    14:00 - 00:00
            Tuesday:   14:00 - 00:00
            Wednesday: 14:00 - 00:00
            Thursday:  14:00 - 00:00
            Friday:    14:00 - 02:00
            Saturday:  14:00 - 02:00
            Sunday:    14:00 - 00:00
            """;

    public final String MAIN_MENU = """
            1. Listed movies
            2. Cinema info
            3. Opening Hours
            4. Exit
            """;


    public final String INNER_MENU = """
            1. Observe available seats
            2. Movie info
            3. Book seat
            4. Cancel seat
            5. Back
            """;
}
