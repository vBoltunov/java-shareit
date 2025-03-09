package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.enums.BookingState;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.HeaderConstants;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private BookingServiceImpl bookingService;

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStartTime(LocalDateTime.now().plusDays(1));
        bookingDto.setEndTime(LocalDateTime.now().plusDays(2));
        bookingDto.setItemId(1L);
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setBooker(new UserDto(1L, "John", "john@example.com"));
        bookingDto.setItem(new ItemDto(
                1L, "Hammer", "A hammer",
                true, 2L, null, null, null, null));
    }

    @Test
    void getBookingsByBookerId_ShouldReturnBookings() throws Exception {
        long bookerId = 1L;
        when(bookingService.getBookingsByBookerIdAndState(bookerId, BookingState.ALL))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header(HeaderConstants.USER_ID_HEADER, bookerId)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].start").exists());
    }

    @Test
    void getBookingsByOwnerId_ShouldReturnBookings() throws Exception {
        long ownerId = 2L;
        when(bookingService.getBookingsByOwnerId(ownerId))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header(HeaderConstants.USER_ID_HEADER, ownerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getBookingById_ShouldReturnBooking() throws Exception {
        long userId = 1L;
        long bookingId = 1L;
        when(bookingService.getBookingById(bookingId, userId)).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{booking-id}", bookingId)
                        .header(HeaderConstants.USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.start").exists());
    }

    @Test
    void createBooking_ShouldCreateBooking() throws Exception {
        long bookerId = 1L;
        BookingDto inputDto = new BookingDto();
        inputDto.setStartTime(LocalDateTime.now().plusDays(1));
        inputDto.setEndTime(LocalDateTime.now().plusDays(2));
        inputDto.setItemId(1L);

        when(bookingService.createBooking(any(BookingDto.class), eq(bookerId))).thenReturn(bookingDto);

        String jsonRequest = objectMapper.writeValueAsString(inputDto);

        mockMvc.perform(post("/bookings")
                        .header(HeaderConstants.USER_ID_HEADER, bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void approveBooking_ShouldApproveBooking() throws Exception {
        long ownerId = 2L;
        long bookingId = 1L;
        bookingDto.setStatus(BookingStatus.APPROVED);
        when(bookingService.approveBooking(bookingId, ownerId, true)).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{booking-id}", bookingId)
                        .header(HeaderConstants.USER_ID_HEADER, ownerId)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }
}
