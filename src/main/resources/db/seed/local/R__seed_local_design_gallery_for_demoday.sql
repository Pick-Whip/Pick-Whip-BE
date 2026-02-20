/* ======================================================
   1. 기존 샵 11번 디자인 관련 데이터 삭제 (초기화)
====================================================== */
DELETE FROM design_gallery_keywords WHERE design_gallery_id IN (111, 112, 113, 114, 115, 116);
DELETE FROM design_options WHERE design_id IN (111, 112, 113, 114, 115, 116);
DELETE FROM avail_option WHERE design_id IN (111, 112, 113, 114, 115, 116);
DELETE FROM design_gallery WHERE id IN (111, 112, 113, 114, 115, 116);

/* ======================================================
   2. 디자인 갤러리 추가 (ID 111~116으로 할당)
====================================================== */
INSERT INTO design_gallery (
    id, shop_id, shop_cake_size_id, design_name, base_price, description,
    allergy_info, image_url, lettering_text, lettering_line_count,
    lettering_alignment, lettering_color
) VALUES
      (111, 11, 12, '크리스마스 파티 케이크', 50000, '- 크림치즈가 들어 있어 냉장보관 필수, 수령 후 24시간 내 섭취 권장', '우유, 밀, 딸기', 'd1.png', 'merry christmas', 'ONE_LINE', 'CENTER', '#FF0000'),
      (112, 11, 11, '발렌타인 하트 케이크', 55000, '- 크림치즈가 들어 있어 냉장보관 필수, 수령 후 24시간 내 섭취 권장', '우유, 밀, 대두', 'd2.png', 'Happy Valentine’s Day!', 'ONE_LINE', 'CENTER', '#FFC0CB'),
      (113, 11, 12, '발렌타인 초코 케이크', 55000, '냉장보관 필수, 수령 후 24시간 내 섭취 권장', '우유, 밀', 'd3.png', 'happy valentine’s day!', 'ONE_LINE', 'CENTER', '#000000'),
      (114, 11, 11, '졸업 축하 케이크', 50000, '생크림이 들어 있어 냉장보관 필수, 수령 후 24시간 내 섭취 권장', '우유, 밀', 'd4.png', 'bir grad', 'ONE_LINE', 'CURVE_UP', '#800080'),
      (115, 11, 12, '퇴사 축하 케이크', 55000, '생크림이 들어 있어 냉장보관 필수, 수령 후 24시간 내 섭취 권장', '우유, 밀', 'd5.png', '회사 그만두겠습니당', 'ONE_LINE', 'CENTER', '#0000FF'),
      (116, 11, 11, '생일 축하 케이크', 50000, '생크림이 들어 있어 냉장보관 필수, 수령 후 24시간 내 섭취 권장', '우유, 밀, 계란, 초콜릿', 'd6.png', 'happy birthday', 'ONE_LINE', 'CENTER', '#4B2C20');

/* ======================================================
   3. 각 디자인별 적용된 옵션 (design_options)
====================================================== */
INSERT INTO design_options (design_id, custom_option_id, position_x, position_y) VALUES
(111, 198,null,null),(111,201,null,null),(111,206,null,null),(111,209,null,null),
(112,200,null,null),(112,201,null,null),(112,206,null,null),(112,212,null,null),
(113,198,null,null),(113,204,null,null),(113,208,null,null),(113,210,null,null),
(114,198,null,null),(114,202,null,null),(114,205,null,null),(114,211,null,null),
(115,198,null,null),(115,204,null,null),(115,205,null,null),(115,209,null,null),
(116,200,null,null),(116,202,null,null),(116,205,null,null),(116,210,null,null);

/* ======================================================
   4. 키워드 매핑 (노션 기반 스타일 분류)
====================================================== */
INSERT INTO design_gallery_keywords (design_gallery_id, keyword) VALUES
                                                                     (111, 'CHRISTMAS'), (111, 'ANNIVERSARY'),(111, 'PARTY'),
                                                                     (112, 'ANNIVERSARY'), (112, 'PARTY'),
                                                                     (113, 'LOVELY'), (113, 'ANNIVERSARY'), (113, 'CHOCOLATE'), (113, 'PARTY'),
                                                                     (114, 'ANNIVERSARY'), (114, 'LETTERING'), (114, 'GRADUATION'),
                                                                     (115, 'MINIMAL'), (115, 'LETTERING'),
                                                                     (116, 'ANNIVERSARY'), (116, 'LETTERING'), (116, 'BIRTHDAY');

/* ======================================================
   5. 선택 가능 옵션 매핑 (avail_option)
   - 샵 11의 모든 마스터 옵션(101~110)을 선택 가능하게 연결
====================================================== */
INSERT INTO avail_option (design_id, custom_option_id)
SELECT d.id, c.id
FROM design_gallery d, custom_options c
WHERE d.id BETWEEN 111 AND 116 AND c.shop_id = 11;

