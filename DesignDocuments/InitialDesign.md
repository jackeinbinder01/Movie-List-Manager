# Design Documents

You may have multiple design documents for this project. Place them all in this folder. File naming is up to you, but it should be clear what the document is about. At the bare minimum, you will want a pre/post UML diagram for the project.


```mermaid
classDiagram
    direction TB

    BaseView ..|> IView: implements
    BaseView --|> JFrame: extends
    BaseView --> FilterPane: uses
    BaseView --> ListPane: uses
    BaseView --> DetailsPane: uses
    FilterPane --|> JPanel: extends
    ListPane --|> JPanel: extends
    DetailsPane --|> JPanel: extends
    Controller ..|> IController: implements
    Controller --> IModel: uses
    Controller --> IView: uses
    Model ..|> IModel: implements
    MBeans --> MBeansViews: uses
    MBeansLoad --> MBeansDeserializer: uses
    MBeansFormatter --> MBeansSerializer: uses
    FilterPane ..|> ActionListener : implements
    FilterPane ..> JTextField : uses
    FilterPane ..> JComboBox : uses
    FilterPane ..> JXMultiThumbSlider : uses
    FilterPane ..> JButton : uses
    FilterPane ..> GridBagConstraints : uses
    FilterPane ..> List : uses
    FilterPane ..> MBeans : uses
    FilterPane ..> JFrame : uses
    FilterPane ..> JPanel : inherits

    namespace MODEL {
        class IModel
        class Model
        class IMovieList
        class MovieData
        class MovieList
        class formatters
        class filter
        class beans
        class net
    }

    namespace formatters {
        class Formats
        class MBeansDeserializer
        class MBeansSerializer
        class MBeansFormatter
        class MBeansLoader
    }

    namespace filter {
        class FilterHandler
        class FilterOperation
        class IFilterHandler
        class Operations
    }

    namespace beans {
        class MBeans
        class MBeansViews
    }

    namespace net {
        class NetUtils
    }

    namespace VIEW {
        class IView
        class BaseView
        class FilterPane
        class ListPane
        class DetailsPane
        class JFrame
    }

    namespace CONTROLLER {
        class IController
        class Controller
        class IFeature
    }



    class Model {

    }


    class IModel {
    << Interface >>
        - DEFAULT_DATA
        loadSourceData() void
        loadWatchList() void
        getMovieList(FilterClass, UserListIdentifier) Stream~MBeans~
        saveWatchList(String, Stream~MBeans~) void
        getFiltered() Stream~MBeans~

    }

    class IController {
        + go(): void

    }

    class IView {
        + display(): void
        + setDetailsPane(): void

    }

    class FilterPane {
        - List~MBeans~ movies
        - JTextField titleFilter
        - JComboBox contentTypeFilter
        - JComboBox genreFilter
        - JComboBox mpaRatingFilter
        - JXMultiThumbSlider releasedFilter
        - JXMultiThumbSlider imdbRatingFilter
        - JXMultiThumbSlider runtimeFilter
        - JXMultiThumbSlider boxOfficeFilter
        - JTextField directorFilter
        - JTextField actorFilter
        - JTextField writerFilter
        - JComboBox languageFilter
        - JComboBox countryOfOriginFilter
        - JButton applyFilterButton
        - JButton cleaFilterButton
        - GridBagConstraints gbc
        - int filterRow
        + FilterPane()
        - addLabel(String filterTitle): void
        - addFilter(String filterTitle, Object filter): void
        - addButton(String buttonTitle, JButton button): void
        - configureThumbSlider(JXMultiThumbSlider thumbSlider): void
        + actionPerformed(ActionEvent e): void
    }

    class ListPane {
        - JPanel listPanel
        - JTable listTable
        - JButton list1
        - JButton list2
        - JButton addList
        - JButton exportList
        - JButton removeCurrentEntry
        + ListPane()
        + addColumns(JTable listTable)
        + fillInTable(JTable listTable)
    }

    class DetailsPane {
        - DateTimeFormatter DATE_FORMAT
        - int DEFAULT_WIDTH
        - int DEFAULT_HEIGHT
        - Color DEFAULT_COLOR
        - JScrollPane scrollPane
        - JPanel detailsPanel
        - JTextOabe mediaTitle
        - JLabel mediaImage
        - List~JTextArea~ mediaDetails
        - JCheckBox watchedBox
        - JTextField userRating
        - JButton saveRating
        + DetailsPane()
        - initContent() void
        - addVerticalPadding(int) void
        - addTitlePane() void
        - addImageLabel() void
        - addWatched() void
        - addDetailPane(String) void
        - addUserRating() void
        - reSize() void
        - scaleImage(URL) ImageIcon
        - setMedia(MBeans) void
        + addListeners() void
    }


    class MBeans {
        - Title: String
        - Year: int
        - Type: String
        - Rated: String
        - Released: Date
        - Runtime: int
        - Genre: String
        - Director: List~String~
        - Writer: List~String~
        - Actors: List~String~
        - Plot: String
        - Language: List~String~
        - Country: String
        - Awards: String
        - Poster: URL
        - HasWatched: bool
        - Metascore: int
        - imdbRating: double
        - myRating: double
        - boxOffice: int
    }

    class MBeansLoader {

    }

    class MBeansFormatter {

    }
```
