package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemWithCommentResponseDto;
import ru.practicum.shareit.item.dto.ItemWithRequestResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mvc;
    private ItemWithRequestResponseDto itemWithRequestResponseDto = new ItemWithRequestResponseDto(
            1L, "TestItemWithRequestName", "TestItemWithRequestDescription",
            true, 10L, null);
    private ItemWithCommentResponseDto itemWithCommentResponseDto = new ItemWithCommentResponseDto(
            1L, "TestItemWithCommentName", "TestItemWithCommentDescription", true);

    @Test
    public void addItemTest() throws Exception {
        when(itemService.add(anyLong(), any()))
                .thenReturn(itemWithRequestResponseDto);

        mvc.perform(MockMvcRequestBuilders.post("/items")
                        .header("X-Sharer-User-Id", itemWithRequestResponseDto.getOwnerId())
                        .content(mapper.writeValueAsString(itemWithRequestResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithRequestResponseDto.getName())))
                .andExpect(jsonPath("$.description", is(itemWithRequestResponseDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemWithRequestResponseDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemWithRequestResponseDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.requestId", is(itemWithRequestResponseDto.getRequestId()), Long.class));
    }

    @Test
    public void updateItemTest() throws Exception {
        when(itemService.update(anyLong(), anyLong(), any()))
                .thenReturn(itemWithRequestResponseDto);

        mvc.perform(MockMvcRequestBuilders.patch("/items/1")
                        .header("X-Sharer-User-Id", itemWithRequestResponseDto.getOwnerId())
                        .content(mapper.writeValueAsString(itemWithRequestResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithRequestResponseDto.getName())))
                .andExpect(jsonPath("$.description", is(itemWithRequestResponseDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemWithRequestResponseDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemWithRequestResponseDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.requestId", is(itemWithRequestResponseDto.getRequestId()), Long.class));
    }

    @Test
    public void getItemTest() throws Exception {
        when(itemService.get(anyLong(), anyLong()))
                .thenReturn(itemWithCommentResponseDto);

        mvc.perform(MockMvcRequestBuilders.get("/items/1")
                        .header("X-Sharer-User-Id", itemWithRequestResponseDto.getOwnerId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithCommentResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithCommentResponseDto.getName())))
                .andExpect(jsonPath("$.description", is(itemWithCommentResponseDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemWithCommentResponseDto.getAvailable())));
    }

    @Test
    public void getUserItemsTest() throws Exception {
        when(itemService.getUserItems(anyLong(), any()))
                .thenReturn(List.of(itemWithCommentResponseDto));

        mvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", itemWithRequestResponseDto.getOwnerId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemWithCommentResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemWithCommentResponseDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemWithCommentResponseDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemWithCommentResponseDto.getAvailable())));
    }

    @Test
    public void searchItemsTest() throws Exception {
        when(itemService.searchItems(anyLong(), anyString()))
                .thenReturn(List.of(itemWithCommentResponseDto));

        mvc.perform(MockMvcRequestBuilders.get("/items/search?text=text")
                        .header("X-Sharer-User-Id", itemWithRequestResponseDto.getOwnerId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemWithCommentResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemWithCommentResponseDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemWithCommentResponseDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemWithCommentResponseDto.getAvailable())));
    }

    @Test
    public void addCommentTest() throws Exception {
        CommentResponseDto commentDto = new CommentResponseDto(
                11L, "TestComment", "TestAuthor", null);
        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mvc.perform(MockMvcRequestBuilders.post("/items/1/comment")
                        .header("X-Sharer-User-Id", 10L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated()), String.class));
    }
}
