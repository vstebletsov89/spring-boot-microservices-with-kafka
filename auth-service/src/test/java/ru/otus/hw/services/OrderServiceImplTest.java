//package ru.otus.hw.services;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.otus.hw.mappers.ItemMapperImpl;
//import ru.otus.hw.mappers.OrderMapperImpl;
//
//@DisplayName("Проверка работы сервиса заказов")
//@SpringBootTest(classes = {OrderServiceImpl.class,
//        OrderMapperImpl.class,
//        ItemMapperImpl.class})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class OrderServiceImplTest {
//
//    //TODO: implement services tests
//
////    private static List<Book> expectedBooks = new ArrayList<>();
////
////    private static List<BookDto> expectedBooksDto = new ArrayList<>();
////
////    @MockBean
////    private AuthorRepository authorRepository;
////
////    @MockBean
////    private GenreRepository genreRepository;
////
////    @MockBean
////    private OrderRepository bookRepository;
////
////    @Autowired
////    private BookService bookService;
////
////    @Autowired
////    private BookMapper bookMapper;
////
////    @BeforeAll
////    void setExpectedBooks() {
////        expectedBooks = List.of(
////                new Book(1L, "TestBook1",
////                        new Author(1L, "TestAuthor1"),
////                        new Genre(1L, "TestGenre1")),
////                new Book(2L, "TestBook2",
////                        new Author(2L, "TestAuthor2"),
////                        new Genre(2L, "TestGenre2")),
////                new Book(3L, "TestBook3",
////                        new Author(3L, "TestAuthor3"),
////                        new Genre(3L, "TestGenre3"))
////        );
////
////        expectedBooksDto =
////                expectedBooks.stream()
////                        .map(bookMapper::toDto)
////                        .toList();
////    }
////
////    @DisplayName("должен загружать список всех книг")
////    @Test
////    void shouldReturnCorrectBooksList() {
////        doReturn(expectedBooks).when(bookRepository).findAll();
////        var actualBooks = bookService.findAll();
////
////        assertEquals(3, actualBooks.size());
////        actualBooks.forEach(System.out::println);
////        assertEquals(expectedBooksDto, actualBooks);
////    }
////
////    @DisplayName("должен загружать книгу по id")
////    @Test
////    void shouldReturnCorrectBookById() {
////        long bookId = 2L;
////        int bookPos = 1;
////        doReturn(Optional.of(expectedBooks.get(bookPos))).when(bookRepository).findById(bookId);
////        var actualBook = bookService.findById(bookId);
////
////        assertThat(actualBook)
////                .usingRecursiveComparison()
////                .isEqualTo(expectedBooksDto.get(bookPos));
////    }
////
////    @DisplayName("должен выбрасывать исключение для неверного id")
////    @Test
////    void shouldReturnExceptionForInvalidId() {
////        doReturn(Optional.empty()).when(bookRepository).findById(99L);
////        var exception = assertThrows(NotFoundException.class,
////                () -> bookService.findById(99L));
////
////        assertEquals("Book with id 99 not found", exception.getMessage());
////    }
////
////
////    @DisplayName("должен сохранять новую книгу")
////    @Test
////    void shouldSaveNewBook() {
////        Book returnedBook = expectedBooks.get(0);
////        Book expectedBook = new Book(
////                returnedBook.getId(),
////                returnedBook.getTitle(),
////                returnedBook.getAuthor(),
////                returnedBook.getGenre()
////        );
////        expectedBook.setId(null);
////        doReturn(expectedBook).when(bookRepository).save(any());
////        doReturn(Optional.of(expectedBook.getAuthor()))
////                .when(authorRepository)
////                .findById(expectedBook.getAuthor().getId());
////        doReturn(Optional.of(expectedBook.getGenre())).when(genreRepository).findById(expectedBook.getGenre().getId());
////        var bookDto = bookService.create(new BookCreateDto(
////                expectedBook.getTitle(),
////                expectedBook.getAuthor().getId(),
////                expectedBook.getGenre().getId()));
////
////        assertThat(bookMapper.toModel(bookDto, expectedBook.getAuthor(), expectedBook.getGenre()))
////                .usingRecursiveComparison()
////                .isEqualTo(expectedBook);
////    }
////
////    @DisplayName("должен обновить книгу")
////    @Test
////    void shouldUpdateBook() {
////        Book newBook = expectedBooks.get(1);
////        doReturn(newBook).when(bookRepository).save(any());
////        doReturn(Optional.of(newBook)).when(bookRepository).findById(2L);
////        doReturn(Optional.of(newBook.getAuthor())).when(authorRepository).findById(newBook.getAuthor().getId());
////        doReturn(Optional.of(newBook.getGenre())).when(genreRepository).findById(newBook.getGenre().getId());
////        var bookDto = bookService.update(new BookUpdateDto(
////                2L,
////                newBook.getTitle(),
////                newBook.getAuthor().getId(),
////                newBook.getGenre().getId()));
////
////        assertThat(bookMapper.toModel(bookDto, newBook.getAuthor(), newBook.getGenre()))
////                .usingRecursiveComparison()
////                .isEqualTo(newBook);
////    }
//}