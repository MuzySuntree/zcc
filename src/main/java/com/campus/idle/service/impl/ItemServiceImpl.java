package com.campus.idle.service.impl;

import com.campus.idle.common.PageResult;
import com.campus.idle.common.ResultCode;
import com.campus.idle.dto.item.ItemCreateDTO;
import com.campus.idle.dto.item.ItemQueryDTO;
import com.campus.idle.dto.item.ItemUpdateDTO;
import com.campus.idle.entity.IdleItem;
import com.campus.idle.entity.ItemCategory;
import com.campus.idle.entity.ItemImage;
import com.campus.idle.entity.SysUser;
import com.campus.idle.exception.BizException;
import com.campus.idle.repository.IdleItemRepository;
import com.campus.idle.repository.ItemCategoryRepository;
import com.campus.idle.repository.ItemImageRepository;
import com.campus.idle.repository.SysUserRepository;
import com.campus.idle.service.ItemService;
import com.campus.idle.util.SecurityUtil;
import com.campus.idle.vo.item.ItemDetailVO;
import com.campus.idle.vo.item.ItemImageVO;
import com.campus.idle.vo.item.ItemSimpleVO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final IdleItemRepository itemRepository;
    private final ItemCategoryRepository categoryRepository;
    private final ItemImageRepository imageRepository;
    private final SysUserRepository userRepository;
    private final SecurityUtil securityUtil;

    public ItemServiceImpl(IdleItemRepository itemRepository,
                           ItemCategoryRepository categoryRepository,
                           ItemImageRepository imageRepository,
                           SysUserRepository userRepository,
                           SecurityUtil securityUtil) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    @Transactional
    public ItemDetailVO create(ItemCreateDTO dto) {
        SysUser currentUser = userRepository.findById(securityUtil.getCurrentUser().getId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "用户不存在"));
        ItemCategory category = categoryRepository.findByIdAndDeleted(dto.getCategoryId(), 0)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "分类不存在"));

        IdleItem item = new IdleItem();
        item.setUser(currentUser);
        item.setCategory(category);
        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());
        item.setConditionLevel(dto.getConditionLevel());
        item.setExpectedItemDesc(dto.getExpectedItemDesc());
        item.setCampusLocation(dto.getCampusLocation());
        item.setContactInfo(dto.getContactInfo());
        item = itemRepository.save(item);

        saveImages(item, dto.getImageUrls());
        return detail(item.getId());
    }

    @Override
    @Transactional
    public ItemDetailVO update(Long id, ItemUpdateDTO dto) {
        IdleItem item = getById(id);
        validateOwnership(item);
        if (item.getStatus() != null && item.getStatus() == 2) {
            throw new BizException(ResultCode.FORBIDDEN, "已同意交换，无法进行编辑！");
        }
        ItemCategory category = categoryRepository.findByIdAndDeleted(dto.getCategoryId(), 0)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "分类不存在"));

        item.setCategory(category);
        item.setTitle(dto.getTitle());
        item.setDescription(dto.getDescription());
        item.setConditionLevel(dto.getConditionLevel());
        item.setExpectedItemDesc(dto.getExpectedItemDesc());
        item.setCampusLocation(dto.getCampusLocation());
        item.setContactInfo(dto.getContactInfo());
        itemRepository.save(item);

        imageRepository.deleteByItemId(item.getId());
        saveImages(item, dto.getImageUrls());
        return detail(item.getId());

    }

    @Override
    @Transactional
    public void delete(Long id) {
        IdleItem item = getById(id);
        validateOwnership(item);
        if (item.getStatus() != null && item.getStatus() == 2) {
            throw new BizException(ResultCode.FORBIDDEN, "已同意交换，无法删除！");
        }
        item.setDeleted(1);
        itemRepository.save(item);
        imageRepository.deleteByItemId(id);
    }

    @Override
    @Transactional
    public ItemDetailVO detail(Long id) {
        IdleItem item = getById(id);
        item.setViewCount(item.getViewCount() + 1);
        itemRepository.save(item);
        return toDetailVO(item);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ItemSimpleVO> page(ItemQueryDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPageNum() - 1, dto.getPageSize());
        Specification<IdleItem> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));

            if (StringUtils.hasText(dto.getKeyword())) {
                predicates.add(cb.or(
                        cb.like(root.get("title"), "%" + dto.getKeyword().trim() + "%"),
                        cb.like(root.get("description"), "%" + dto.getKeyword().trim() + "%")
                ));
            }

            if (dto.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), dto.getCategoryId()));
            }

            if (dto.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), dto.getStatus()));
            }

            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<IdleItem> page = itemRepository.findAll(specification, pageable);
        List<ItemSimpleVO> records = page.getContent().stream().map(this::toSimpleVO).toList();

        return PageResult.<ItemSimpleVO>builder()
                .records(records)
                .total(page.getTotalElements())
                .pageNum(dto.getPageNum())
                .pageSize(dto.getPageSize())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ItemSimpleVO> myPage(ItemQueryDTO dto) {
        SysUser currentUser = securityUtil.getCurrentUser();

        Pageable pageable = PageRequest.of(dto.getPageNum() - 1, dto.getPageSize());
        Specification<IdleItem> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));
            predicates.add(cb.equal(root.get("user").get("id"), currentUser.getId()));

            if (StringUtils.hasText(dto.getKeyword())) {
                predicates.add(cb.or(
                        cb.like(root.get("title"), "%" + dto.getKeyword().trim() + "%"),
                        cb.like(root.get("description"), "%" + dto.getKeyword().trim() + "%")
                ));
            }

            if (dto.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), dto.getCategoryId()));
            }

            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<IdleItem> page = itemRepository.findAll(specification, pageable);
        List<ItemSimpleVO> records = page.getContent().stream().map(this::toSimpleVO).toList();

        return PageResult.<ItemSimpleVO>builder()
                .records(records)
                .total(page.getTotalElements())
                .pageNum(dto.getPageNum())
                .pageSize(dto.getPageSize())
                .totalPages(page.getTotalPages())
                .build();
    }
    @Override
    @Transactional(readOnly = true)
    public PageResult<ItemSimpleVO> adminPage(ItemQueryDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPageNum() - 1, dto.getPageSize());

        Specification<IdleItem> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), 0));

            if (StringUtils.hasText(dto.getKeyword())) {
                predicates.add(cb.or(
                        cb.like(root.get("title"), "%" + dto.getKeyword().trim() + "%"),
                        cb.like(root.get("description"), "%" + dto.getKeyword().trim() + "%")
                ));
            }

            if (dto.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), dto.getCategoryId()));
            }

            // 管理员页面：如果传了状态就筛选，不传就看全部状态
            if (dto.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), dto.getStatus()));
            }

            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<IdleItem> page = itemRepository.findAll(specification, pageable);
        List<ItemSimpleVO> records = page.getContent().stream().map(this::toSimpleVO).toList();

        return PageResult.<ItemSimpleVO>builder()
                .records(records)
                .total(page.getTotalElements())
                .pageNum(dto.getPageNum())
                .pageSize(dto.getPageSize())
                .totalPages(page.getTotalPages())
                .build();
    }
    private IdleItem getById(Long id) {
        return itemRepository.findByIdAndDeleted(id, 0)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "物品不存在"));
    }

    private void validateOwnership(IdleItem item) {
        SysUser currentUser = securityUtil.getCurrentUser();
        boolean isOwner = currentUser.getId().equals(item.getUser().getId());
        if (!isOwner && !securityUtil.isAdmin(currentUser)) {
            throw new BizException(ResultCode.FORBIDDEN, "只能操作自己的物品");
        }
    }

    private void saveImages(IdleItem item, List<String> imageUrls) {
        for (int i = 0; i < imageUrls.size(); i++) {
            ItemImage image = new ItemImage();
            image.setItem(item);
            image.setImageUrl(imageUrls.get(i));
            image.setSortNo(i + 1);
            image.setIsCover(i == 0 ? 1 : 0);
            imageRepository.save(image);
        }
    }

    private ItemSimpleVO toSimpleVO(IdleItem item) {
        String coverImage = imageRepository.findByItemIdOrderBySortNoAscIdAsc(item.getId())
                .stream()
                .findFirst()
                .map(ItemImage::getImageUrl)
                .orElse(null);

        return ItemSimpleVO.builder()
                .id(item.getId())
                .title(item.getTitle())
                .categoryName(item.getCategory().getCategoryName())
                .ownerNickname(item.getUser().getNickname())
                .conditionLevel(item.getConditionLevel())
                .status(item.getStatus())
                .coverImage(coverImage)
                .createdAt(item.getCreatedAt())
                .build();
    }

    private ItemDetailVO toDetailVO(IdleItem item) {
        List<ItemImageVO> images = imageRepository.findByItemIdOrderBySortNoAscIdAsc(item.getId())
                .stream()
                .map(image -> ItemImageVO.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .sortNo(image.getSortNo())
                        .isCover(image.getIsCover())
                        .build())
                .toList();

        return ItemDetailVO.builder()
                .id(item.getId())
                .ownerId(item.getUser().getId())
                .ownerNickname(item.getUser().getNickname())
                .categoryId(item.getCategory().getId())
                .categoryName(item.getCategory().getCategoryName())
                .title(item.getTitle())
                .description(item.getDescription())
                .conditionLevel(item.getConditionLevel())
                .expectedItemDesc(item.getExpectedItemDesc())
                .campusLocation(item.getCampusLocation())
                .contactInfo(item.getContactInfo())
                .status(item.getStatus())
                .viewCount(item.getViewCount())
                .createdAt(item.getCreatedAt())
                .images(images)
                .build();
    }
}