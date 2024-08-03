# Movie List Manager: Final Design UML Class Diagram

```mermaid
classDiagram
    direction LR

    namespace MODEL {
        class IModel
        class Model
        class IMovieList
        class MovieData
        class MovieList

    }

    namespace VIEW {
        class IView
        class BaseView
        class FilterPane
        class FilterPaneLabels
        class Filters
        class ListPane
        class DetailsPane
        class AppFont
    }

    namespace CONTROLLER {
        class IController
        class IFeature
        class Controller
        class ErrorMessage
    }

    namespace Formatters {
        class MovieXMLWrapper
        class Formats
        class MBeansDeserializer
        class MBeansSerializer
        class MBeansFormatter
        class MBeansLoader
        class IntSerializer
        class DoubleSerializer
        class DateSerializer
        class RuntimeSerializer
        class StringListSerializer
        class BoxOfficeSerializer
        class IntDeserializer
        class DoubleDeserializer
        class DateDeserializer
        class RuntimeDeserializer
        class StringListDeserializer
        class BoxOfficeDeserializer
    }

    namespace Filters {
        class FilterHandler
        class FilterOperation
        class IFilterHandler
        class Operations
    }

    namespace Movies {
        class MBeans
    }

    namespace Net {
        class NetUtils
        class APIBeans
        class MovieAPIHandler
    }
    
    namespace APP {
        class MovieListManager
    }
    
    MovieListManager --> IModel: uses
    MovieListManager --> IView: uses
    MovieListManager --> IController: uses
    
    Model ..|> IModel: implements
    BaseView ..|> IView: implements
    Controller ..|> IController: implements
    Controller ..|> IFeature: implements
    MovieList ..|> IMovieList: implements
    
    Model --> MovieData: uses
    Model --> MovieList: uses
    
    Controller --> IModel: uses
    Controller --> IView: uses
    
    BaseView --> FilterPane: uses
    BaseView --> ListPane: uses
    BaseView --> DetailsPane: uses
    BaseView --> AppFont: uses
    
    Controller --> ErrorMessage: uses

    MBeansDeserializer --|> IntDeserializer : contains
    MBeansDeserializer --|> DoubleDeserializer : contains
    MBeansDeserializer --|> DateDeserializer : contains
    MBeansDeserializer --|> RuntimeDeserializer : contains
    MBeansDeserializer --|> StringListDeserializer : contains
    MBeansDeserializer --|> BoxOfficeDeserializer : contains

    MBeansSerializer --|> IntSerializer : contains
    MBeansSerializer --|> DoubleSerializer : contains
    MBeansSerializer --|> DateSerializer : contains
    MBeansSerializer --|> RuntimeSerializer : contains
    MBeansSerializer --|> StringListSerializer : contains
    MBeansSerializer --|> BoxOfficeSerializer : contains
    
    
    class MovieListManager { 
        - MovieListManager()
        - main(String[] args): void$
    }
    
    class IModel {
        <<interface>>
        + DEFAULT_DATA: String$
        + loadSourceData(): void
        + loadWatchList(String filename): int
        + createNewWatchList(String name): int
        + getRecords(): Stream~MBeans~
        + getRecords(int userListId): Stream~MBeans~
        + getRecords(List~List~String~~ filters): Stream~MBeans~
        + getRecords(int userListId, List~List~String~~ filters): Stream~MBeans~
        + saveWatchList(String filename, int userListId/*, UserListIdentifier userListId*/): void
        + addToWatchList(MBeans media, int userListId): void
        + removeFromWatchList(MBeans media, int userListId): void
        + updateWatched(MBeans media, boolean watched): void
        + updateUserRating(MBeans media, double rating): void
        + updateSourceList(): void
        + updateSourceList(Set~MBeans~ moviesToAdd): void
        + getUserListName(int userListId): String
        + getUserListCount(): int
        + getUserListIndicesForRecord(MBeans record): int[]
        + clearFilter(): void
        + addNewMBeans(List~List~String~~ filters, Stream<MBeans> movieStream): void
        + extractFilterValues(List~List~String~~ filters): Map<String, String>
        + fetchMBeans(String title, String year1, String year2): Set~<MBeans~
    }
    
    class IView {
        <<interface>>
        + setDetailsPaneEntry(MBeans record): void
        + clearTableSelection(): void
        + setSourceTableRecords(Stream~MBeans~ records, String[] watchlistNames, boolean[][] recordWatchlistMatrix): void
        + setUserTableRecords(Stream~MBeans~ records, int userListId): void
        + addUserTable(String watchlistName): void
        + bindFeatures(IFeature features): void
        + getFilterPane(): FilterPane
        + getDetailsPane(): DetailsPane
        + display(): void
        + getCurrentTab(): int
    }
    
    class IController {
        <<interface>>
        + go(): void
    }
    
    class IFeature {
        <<interface>>
        + handleTableSelection(MBeans record): void
        + importListFromFile(String filepath): void
        + exportListToFile(String filepath): void
        + removeFromWatchlist(MBeans mbean, int userListIndex): void
        + addToWatchlist(MBeans mbean, int userListIndex): void
        + createWatchlist(String name): void
        + deleteWatchlist(int userListIndex): void
        + changeRating(MBeans mbean, double rating): void
        + changeWatchedStatus(MBeans record, boolean watched, String caller): void
        + applyFilters(): void
        + clearFiltersAndReloadRecords(): void
        + handleTabChange(int tabIndex): void
    }
    
    class BaseView {
        - APP_TITLE: String$
        - DEFAULT_WIDTH: int
        - DEFAULT_HEIGHT: int
        + FilterPane
        + ListPane
        + DetailsPane
        + BaseView()
    }

    class Model {
        - sourceList: Set~MBeans~
        - watchLists: List~IMovieList~
        - filterHandler: IFilterHandler
        - filter: List~List~String~~
        + Model()
        + addNewMBeansToSource(Set~MBeans~ newMBeans): void
        + setFilter(List~List~String~~ filter): void
        + getMatchedObjectFromSource(MBeans media): MBeans
    }
    
    class MovieData {
        <<enumeration>>
        + TITLE: MovieData
        + NUMBER: MovieData
        + GENRE: MovieData
        + DIRECTOR: MovieData
        + ACTOR: MovieData
        + LANGUAGE: MovieData
        + WRITER: MovieData
        + MPA: MovieData
        + IMDB: MovieData
        + USER: MovieData
        + RELEASED: MovieData
        + RUNTIME: MovieData
        + BOXOFFICE: MovieData
        + HASWATCHED: MovieData
        - columnTitle: String
        + MovieData(String columnTitle)
        + getColumnTitle(): String
        + fromColumnName(String columnName): MovieData$
        + fromString(String title): MovieData$
    }
    
    class IMovieList {
        <<interface>>
        + getListName(): String
        + getMovieList(): Stream ~MBeans~
        + clear(): void
        + count(): int
        + saveMovie(String filename, Formats format): void
        + addToList(MBeans media): void
        + removeFromList(MBeans media): void
        + containsMedia(MBeans media): boolean
    }
    
    class MovieList {
        - movieList: Set~MBeans~
        - name: String
        + MovieList(String name, Set~MBeans~ movieList)
    }
    
    class Controller {
        + IModel
        + IView
        + Controller(IModel model, IView view)
        - DEV_InitModel(): void
        - getRecordsForCurrentView(): Stream~MBeans~
        - getRecordUserListMatrix(Stream<MBeans> records): boolean[][]
        - getWatchlistNames(): String[]
        - getFilterOptions(): List~List~String~~
    }
    
    class ErrorMessage {
        <<enumeration>>
        + ERROR: ErrorMessage
        + DELETE_WATCHLIST: ErrorMessage
        + CREATE_WATCHLIST: ErrorMessage
        + IMPORT_WATCHLIST: ErrorMessage
        - errorMessage: String
        + ErrorMessage(String errorMessage)
        + getErrorMessage(String filepath): String
    }
    
    class MovieXMLWrapper {
        - movie: Collection~MBeans~
        + MovieXMLWrapper(Collection~MBeans~ movies)
    }
    
    class Formats {
        <<enumeration>>
        + JSON: Formats
        + XML: Formats
        + CSV: Formats
        + PRETTY: Formats
        + containsValues(String value): Formats$
    }
    
    class MBeansDeserializer {
        + IntDeserializer
        + DoubleDeserializer
        + DateDeserializer
        + RuntimeDeserializer
        + StringListDeserializer
        + BoxOfficeDeserializer
    }
    
    class IntDeserializer { 
        + deserialize(JsonParser p, DeserializationContext ctxt): Integer
    }
    
    class DoubleDeserializer {
        + deserialize(JsonParser p, DeserializationContext ctxt): Double
    }

    class DateDeserializer {
        + deserialize(JsonParser p, DeserializationContext ctxt): LocalDate
    }

    class RuntimeDeserializer {
        + deserialize(JsonParser p, DeserializationContext ctxt): Integer
    }

    class StringListDeserializer {
        + deserialize(JsonParser p, DeserializationContext ctxt): List~String~
    }

    class BoxOfficeDeserializer {
        + deserialize(JsonParser p, DeserializationContext ctxt): Integer
    }
    
    class MBeansSerializer {
        + IntSerializer
        + DoubleSerializer
        + DateSerializer
        + RuntimeSerializer
        + StringListSerializer
        + BoxOfficeSerializer
    }
    
    class IntSerializer {
        + serialize(Integer value, JsonGenerator gen, SerializerProvider serializers): void
    }

    class DoubleSerializer {
        + serialize(Double value, JsonGenerator gen, SerializerProvider serializers): void
    }

    class DateSerializer {
        + serialize(LocalDate date, JsonGenerator gen, SerializerProvider serializers): void
    }

    class RuntimeSerializer {
        + serialize(Integer runtime, JsonGenerator gen, SerializerProvider serializers): void
    }

    class StringListSerializer {
        + serialize(List~String~ genre, JsonGenerator gen, SerializerProvider serializers): void
    }

    class BoxOfficeSerializer {
        + serialize(Integer boxOffice, JsonGenerator gen, SerializerProvider serializers): void
    }
    
    class MBeansFormatter {
        - MBeansFormatter()
        - writeMediasToJSON(Collection~MBeans~ records, OutputStream out): void
        - writeMediasToCSV(Collection~MBeans~ records, OutputStream out): void
        - writeMediasToFile(Collection~MBeans~ records, OutputStream out, Formats format): void
    }
    
    class MBeansLoader {
        - MBeansLoader()
        + loadMBeansFromAPI(String title, String year, String type): List~MBeans~$
        + loadMediasFromJSON(String filename): Set~MBeans~
        + loadMediasFromCSV(String filename): Set~MBeans~
        + loadMediasFromFile(String filename, Formats format): Set~MBeans~
    }
    
    
    
    
    

```