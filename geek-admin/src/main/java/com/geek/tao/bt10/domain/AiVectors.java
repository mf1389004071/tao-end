package com.geek.tao.bt10.domain;

import java.time.Instant;
import com.geek.common.annotation.Excel;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AI向量对象 ai_vectors
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Table("ai_vectors")
@Schema(title = "AI向量对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class AiVectors extends BaseEntity
{


    /** $column.columnComment */
    @Schema(title = "$column.columnComment")
    @Id
    private Long id;

    /** $column.columnComment */
    @Schema(title = "$column.columnComment")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String contentChunk;

    /** $column.columnComment */
    @Schema(title = "$column.columnComment")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String embedding;

    /** $column.columnComment */
    @Schema(title = "$column.columnComment")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String metadata;

    /** $column.columnComment */
    @Schema(title = "$column.columnComment")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String sourceType;

    /** $column.columnComment */
    @Schema(title = "$column.columnComment")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long sourceId;

    /** $column.columnComment */
    @Schema(title = "$column.columnComment")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Instant createdAt;

    /** $column.columnComment */
    @Schema(title = "$column.columnComment")
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Instant updatedAt;
}
