package com.geek.flowable.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geek.common.annotation.Log;
import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.core.page.TableDataInfo;
import com.geek.common.enums.BusinessType;
import com.geek.common.utils.poi.ExcelUtil;
import com.geek.flowable.domain.SysListener;
import com.geek.flowable.service.ISysListenerService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 流程监听Controller
 *
 * @author Tony
 * @date 2022-12-25
 */
@RestController
@RequestMapping("/system/listener")
public class SysListenerController extends BaseController {
    @Autowired
    private ISysListenerService sysListenerService;

    /**
     * 查询流程监听列表
     */
    @PreAuthorize("@ss.hasPermi('system:listener:list')")
    @GetMapping("/list")
    public TableDataInfo<SysListener> list(SysListener sysListener) {
        startPage();
        List<SysListener> list = sysListenerService.selectSysListenerList(sysListener);
        return getDataTable(list);
    }

    /**
     * 导出流程监听列表
     */
    @PreAuthorize("@ss.hasPermi('system:listener:export')")
    @Log(title = "流程监听", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysListener sysListener) {
        List<SysListener> list = sysListenerService.selectSysListenerList(sysListener);
        ExcelUtil<SysListener> util = new ExcelUtil<>(SysListener.class);
        util.exportExcel(response, list, "流程监听数据");
    }

    /**
     * 获取流程监听详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:listener:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysListenerService.selectSysListenerById(id));
    }

    /**
     * 新增流程监听
     */
    @PreAuthorize("@ss.hasPermi('system:listener:add')")
    @Log(title = "流程监听", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysListener sysListener) {
        return toAjax(sysListenerService.insertSysListener(sysListener));
    }

    /**
     * 修改流程监听
     */
    @PreAuthorize("@ss.hasPermi('system:listener:edit')")
    @Log(title = "流程监听", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysListener sysListener) {
        return toAjax(sysListenerService.updateSysListener(sysListener));
    }

    /**
     * 删除流程监听
     */
    @PreAuthorize("@ss.hasPermi('system:listener:remove')")
    @Log(title = "流程监听", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysListenerService.deleteSysListenerByIds(ids));
    }
}
